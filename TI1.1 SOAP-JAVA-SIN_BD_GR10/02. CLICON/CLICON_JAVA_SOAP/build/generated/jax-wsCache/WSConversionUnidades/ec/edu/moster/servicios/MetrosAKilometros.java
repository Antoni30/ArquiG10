
package ec.edu.moster.servicios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para metrosAKilometros complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="metrosAKilometros"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="metros" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metrosAKilometros", propOrder = {
    "metros"
})
public class MetrosAKilometros {

    protected double metros;

    /**
     * Obtiene el valor de la propiedad metros.
     * 
     */
    public double getMetros() {
        return metros;
    }

    /**
     * Define el valor de la propiedad metros.
     * 
     */
    public void setMetros(double value) {
        this.metros = value;
    }

}
