package com.example.soap.endpoint;

import com.example.soap.calculator.AddRequest;
import com.example.soap.calculator.AddResponse;
import com.example.soap.calculator.DivideRequest;
import com.example.soap.calculator.DivideResponse;
import com.example.soap.calculator.MultiplyRequest;
import com.example.soap.calculator.MultiplyResponse;
import com.example.soap.calculator.StatusRequest;
import com.example.soap.calculator.StatusResponse;
import com.example.soap.calculator.SubtractRequest;
import com.example.soap.calculator.SubtractResponse;
import com.example.soap.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

@Endpoint
public class CalculatorEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/soap/calculator";

    private final CalculatorService calculatorService;

    @Autowired
    public CalculatorEndpoint(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addRequest")
    @ResponsePayload
    public AddResponse add(@RequestPayload AddRequest request) {
        AddResponse response = new AddResponse();
        response.setResult(calculatorService.add(request.getA(), request.getB()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "subtractRequest")
    @ResponsePayload
    public SubtractResponse subtract(@RequestPayload SubtractRequest request) {
        SubtractResponse response = new SubtractResponse();
        response.setResult(calculatorService.subtract(request.getA(), request.getB()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "multiplyRequest")
    @ResponsePayload
    public MultiplyResponse multiply(@RequestPayload MultiplyRequest request) {
        MultiplyResponse response = new MultiplyResponse();
        response.setResult(calculatorService.multiply(request.getA(), request.getB()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "divideRequest")
    @ResponsePayload
    public DivideResponse divide(@RequestPayload DivideRequest request) {
        DivideResponse response = new DivideResponse();
        response.setResult(calculatorService.divide(request.getA(), request.getB()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "statusRequest")
    @ResponsePayload
    public StatusResponse status(@RequestPayload StatusRequest request) {
        StatusResponse response = new StatusResponse();
        response.setStatus("UP");
        
        try {
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(
                    LocalDateTime.now().atZone(ZoneId.systemDefault()));
            XMLGregorianCalendar xmlGregorianCalendar = 
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            response.setTimestamp(xmlGregorianCalendar);
        } catch (Exception e) {
            // Handle exception
        }
        
        return response;
    }
}