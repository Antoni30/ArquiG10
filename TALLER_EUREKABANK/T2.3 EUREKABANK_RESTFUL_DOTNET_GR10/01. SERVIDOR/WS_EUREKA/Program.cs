var builder = WebApplication.CreateBuilder(args);

// Añadir servicios al contenedor
builder.Services.AddControllers();

var app = builder.Build();

// Configurar pipeline HTTP
app.UseRouting();
app.MapControllers();

app.Run();