
package ec.edu.moster.servicios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para kilogramoALibra complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="kilogramoALibra"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="kilogramos" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "kilogramoALibra", propOrder = {
    "kilogramos"
})
public class KilogramoALibra {

    protected double kilogramos;

    /**
     * Obtiene el valor de la propiedad kilogramos.
     * 
     */
    public double getKilogramos() {
        return kilogramos;
    }

    /**
     * Define el valor de la propiedad kilogramos.
     * 
     */
    public void setKilogramos(double value) {
        this.kilogramos = value;
    }

}
