
package ec.edu.monster.servicios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para gramoAKilogramo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="gramoAKilogramo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="gramos" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gramoAKilogramo", propOrder = {
    "gramos"
})
public class GramoAKilogramo {

    protected double gramos;

    /**
     * Obtiene el valor de la propiedad gramos.
     * 
     */
    public double getGramos() {
        return gramos;
    }

    /**
     * Define el valor de la propiedad gramos.
     * 
     */
    public void setGramos(double value) {
        this.gramos = value;
    }

}
