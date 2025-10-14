//
// Este arquivo foi gerado pela Eclipse Implementation of JAXB, v3.0.0 
// Consulte https://eclipse-ee4j.github.io/jaxb-ri 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2025.10.14 às 10:21:08 AM BRT 
//


package com.example.soap.calculator;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="a" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="b" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "a",
    "b"
})
@XmlRootElement(name = "addRequest")
public class AddRequest {

    protected int a;
    protected int b;

    /**
     * Obtém o valor da propriedade a.
     * 
     */
    public int getA() {
        return a;
    }

    /**
     * Define o valor da propriedade a.
     * 
     */
    public void setA(int value) {
        this.a = value;
    }

    /**
     * Obtém o valor da propriedade b.
     * 
     */
    public int getB() {
        return b;
    }

    /**
     * Define o valor da propriedade b.
     * 
     */
    public void setB(int value) {
        this.b = value;
    }

}
