/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controlador;

import ec.edu.monster.vista.WebServiciosSoap;
import ec.edu.monster.modelo.ConversionUnidades;
/**
 *
 * @author rodri
 */
public class ConversionUnidadesController {
    
    private final   WebServiciosSoap ws ;
    private final ConversionUnidades cu;
    private double resultado;
    public ConversionUnidadesController(WebServiciosSoap serviciosSoap, ConversionUnidades conversionUnidades) {
        this.ws = serviciosSoap;
        this.cu = conversionUnidades;
    }
    
    public void conversion(){
        switch (ws.getTipoConversion()) {
          case "Centimetros a Metros":
              this.resultado = cu.centimetrosAMetros(ws.getValor());
              if (this.resultado==-1){
                  ws.setResultado("No se permite valores Negativos");
              }else{
                  ws.setResultado(ws.getValor()+"cm = "+resultado+"m");
              }
                break;
           case "Metros  a Centimetros":
               this.resultado=cu.metrosACentimetros(ws.getValor());            
               if (this.resultado==-1){
                  ws.setResultado("No se permite valores Negativos");
              }else{
                  ws.setResultado(ws.getValor()+"m = "+resultado+"cm");
              }
                break;
                
          case "Metros a Kilometros":
               this.resultado=cu.metrosAKilometros(ws.getValor());
               if (this.resultado==-1){
                  ws.setResultado("No se permite valores Negativos");
              }else{
                  ws.setResultado(ws.getValor()+"m = "+resultado+"km");
              }
                break;
                
          case "Tonelada a Libra":
               this.resultado=cu.toneladaALibra(ws.getValor());
                  if (this.resultado==-1){
                  ws.setResultado("No se permite valores Negativos");
              }else{
                  ws.setResultado(ws.getValor()+"ton = "+resultado+"lb");
              }
                break;
          case "Kilogramo a Libra":
              
               this.resultado=cu.kilogramoALibra(ws.getValor());
                 if (this.resultado==-1){
                  ws.setResultado("No se permite valores Negativos");
              }else{
                  ws.setResultado(ws.getValor()+"kg = "+resultado+"lb");
              }
                break;
                
           case "Gramo a Kilogramo":
                this.resultado=cu.gramoAKilogramo(ws.getValor());
                  if (this.resultado==-1){
                  ws.setResultado("No se permite valores Negativos");
              }else{
                  ws.setResultado(ws.getValor()+"g = "+resultado+"kg");
              }
                break;
                
                
            case "Celsius a Fahrenheit":
                 this.resultado=cu.celsiusAFahrenheit(ws.getValor());
                    if (this.resultado==-1){
                  ws.setResultado("No se permite valores Negativos");
              }else{
                  ws.setResultado(ws.getValor()+"°C = "+resultado+"°F");
              }
                break;
                
           case "Fahrenheit a Celsius":
                this.resultado=cu.fahrenheitACelsius(ws.getValor());
                
                  if (this.resultado==-1){
                  ws.setResultado("No se permite valores Negativos");
              }else{
                  ws.setResultado(ws.getValor()+"°F = "+resultado+"°C");
              }

                break;
                
           case "Celsius a Kelvin":
                this.resultado=cu.celsiusAKelvin(ws.getValor());
                  if (this.resultado==-1){
                  ws.setResultado("No se permite valores Negativos");
              }else{
                  ws.setResultado(ws.getValor()+"°C = "+resultado+"K");
              }

                break;
          default:
                throw new AssertionError();
        }
    }
}
