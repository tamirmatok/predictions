//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.31 at 12:48:59 PM IDT 
//


package engine.schema.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}PRD-actions"/>
 *         &lt;element ref="{}PRD-activation" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prdActions",
    "prdActivation"
})
@XmlRootElement(name = "PRD-rule")
public class PRDRule {

    @XmlElement(name = "PRD-actions", required = true)
    protected engine.schema.generated.PRDActions prdActions;
    @XmlElement(name = "PRD-activation")
    protected engine.schema.generated.PRDActivation prdActivation;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the prdActions property.
     * 
     * @return
     *     possible object is
     *     {@link engine.schema.generated.PRDActions }
     *     
     */
    public engine.schema.generated.PRDActions getPRDActions() {
        return prdActions;
    }

    /**
     * Sets the value of the prdActions property.
     * 
     * @param value
     *     allowed object is
     *     {@link engine.schema.generated.PRDActions }
     *     
     */
    public void setPRDActions(PRDActions value) {
        this.prdActions = value;
    }

    /**
     * Gets the value of the prdActivation property.
     * 
     * @return
     *     possible object is
     *     {@link engine.schema.generated.PRDActivation }
     *     
     */
    public engine.schema.generated.PRDActivation getPRDActivation() {
        return prdActivation;
    }

    /**
     * Sets the value of the prdActivation property.
     * 
     * @param value
     *     allowed object is
     *     {@link engine.schema.generated.PRDActivation }
     *     
     */
    public void setPRDActivation(PRDActivation value) {
        this.prdActivation = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
