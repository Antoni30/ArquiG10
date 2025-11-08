
package ec.edu.moster.servicios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para toneladaALibra complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="toneladaALibra"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="toneladas" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "toneladaALibra", propOrder = {
    "toneladas"
})
public class ToneladaALibra {

    protected double toneladas;

    /**
     * Obtiene el valor de la propiedad toneladas.
     * 
     */
    public double getToneladas() {
        return toneladas;
    }

    /**
     * Define el valor de la propiedad toneladas.
     * 
     */
    public void setToneladas(double value) {
        this.toneladas = value;
    }

}
