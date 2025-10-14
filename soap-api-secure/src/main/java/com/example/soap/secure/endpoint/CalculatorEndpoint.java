package com.example.soap.secure.endpoint;

import com.example.soap.secure.generated.AddRequest;
import com.example.soap.secure.generated.AddResponse;
import com.example.soap.secure.generated.DivideRequest;
import com.example.soap.secure.generated.DivideResponse;
import com.example.soap.secure.generated.MultiplyRequest;
import com.example.soap.secure.generated.MultiplyResponse;
import com.example.soap.secure.generated.StatusRequest;
import com.example.soap.secure.generated.StatusResponse;
import com.example.soap.secure.generated.SubtractRequest;
import com.example.soap.secure.generated.SubtractResponse;
import com.example.soap.secure.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static final String NAMESPACE_URI = "http://example.com/soap/calculator/secure";

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
        
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "anonymous";
        response.setUser(username);
        
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