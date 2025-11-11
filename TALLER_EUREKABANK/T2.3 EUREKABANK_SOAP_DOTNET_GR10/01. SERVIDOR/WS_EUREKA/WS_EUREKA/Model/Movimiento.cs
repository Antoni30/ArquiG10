using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Xml.Serialization;

namespace WS_EUREKA.Model
{
    [XmlRoot("movimiento")]
    public class Movimiento
    {
        [XmlElement("cuenta")]
        public string Cuenta { get; set; }

        [XmlElement("nromov")]
        public int NroMov { get; set; }

        [XmlElement("fecha")]
        public DateTime Fecha { get; set; }

        [XmlElement("tipo")]
        public string Tipo { get; set; }

        [XmlElement("accion")]
        public string Accion { get; set; }

        [XmlElement("importe")]
        public double Importe { get; set; }

        // Constructor sin parámetros
        public Movimiento() { }

        // Constructor con parámetros
        public Movimiento(string cuenta, int nromov, DateTime fecha, string tipo, string accion, double importe)
        {
            Cuenta = cuenta;
            NroMov = nromov;
            Fecha = fecha;
            Tipo = tipo;
            Accion = accion;
            Importe = importe;
        }
    }
}