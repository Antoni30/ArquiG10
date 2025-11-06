using UnitConverterAPI.Services;

var builder = WebApplication.CreateBuilder(args);

// Configurar Kestrel para escuchar en todas las interfaces
builder.WebHost.ConfigureKestrel(serverOptions =>
{
    serverOptions.ListenAnyIP(5000);    // HTTP
    serverOptions.ListenAnyIP(7000, listenOptions =>
    {
        listenOptions.UseHttps();      // HTTPS
    });
});

// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new Microsoft.OpenApi.Models.OpenApiInfo
    {
        Title = "Unit Converter API",
        Version = "v1.0",
        Description = "API RESTful para conversión de unidades entre diferentes sistemas de medida",
        Contact = new Microsoft.OpenApi.Models.OpenApiContact
        {
            Name = "Equipo de Desarrollo",
            Email = "dev@unitconverter.com"
        },
        License = new Microsoft.OpenApi.Models.OpenApiLicense
        {
            Name = "MIT License"
        }
    });
});

// Registrar nuestro servicio de conversión
builder.Services.AddScoped<IConversionService, ConversionService>();

// Add CORS para permitir peticiones desde clientes
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll", policy =>
    {
        policy.AllowAnyOrigin()
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI(c =>
    {
        c.SwaggerEndpoint("/swagger/v1/swagger.json", "Unit Converter API v1.0");
        c.RoutePrefix = "swagger"; // Accede en /swagger
    });
}

app.UseHttpsRedirection();
app.UseCors("AllowAll");
app.UseAuthorization();
app.MapControllers();

Console.WriteLine("Unit Converter API v1.0 iniciándose...");
Console.WriteLine("Disponible en: http://0.0.0.0:5000 y https://0.0.0.0:7000");
Console.WriteLine("Swagger UI en: http://0.0.0.0:5000/swagger y https://0.0.0.0:7000/swagger");
Console.WriteLine("Accesible desde cualquier máquina en la red");

app.Run();