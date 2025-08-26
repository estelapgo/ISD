package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.util.Purchase.Purchase;
import es.udc.ws.app.model.util.matchservice.MatchServiceFactory;
import es.udc.ws.app.model.util.matchservice.exceptions.AlreadyCollectedException;
import es.udc.ws.app.model.util.matchservice.exceptions.IncorrectBankCardException;
import es.udc.ws.app.model.util.matchservice.exceptions.MatchNotAvailableException;
import es.udc.ws.app.model.util.matchservice.exceptions.NotEnoughTicketsException;
import es.udc.ws.app.restservice.dto.PurchaseToRestPurchaseDtoConversor;
import es.udc.ws.app.restservice.dto.RestPurchaseDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestPurchaseDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseServlet extends RestHttpServletTemplate {
    public PurchaseServlet() {
    }
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException, InstanceNotFoundException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+/collected")) {
            Long purchaseId = Long.valueOf(req.getPathInfo().substring(1).split("/")[0]);
            String bankCard = ServletUtils.getMandatoryParameter(req, "bankCard");

            try {

                MatchServiceFactory.getService().collectedTicket(purchaseId, bankCard);

            } catch (IncorrectBankCardException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toIncorrectBankCardException(ex), null);
            } catch (AlreadyCollectedException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toAlreadyCollectedException(ex), null);
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);

        } else {
            ServletUtils.checkEmptyPath(req);

            Long matchId = ServletUtils.getMandatoryParameterAsLong(req, "matchId");
            int numTickets = Integer.parseInt(ServletUtils.getMandatoryParameter(req, "numTickets"));
            String creditCardNumber = ServletUtils.getMandatoryParameter(req, "creditCardNumber");
            String userEmail = ServletUtils.getMandatoryParameter(req, "userEmail");

            try {
                Purchase purchase = MatchServiceFactory.getService().buyTickets(userEmail, creditCardNumber, numTickets, matchId);
                RestPurchaseDto purchaseDto = PurchaseToRestPurchaseDtoConversor.toRestPurchaseDto(purchase);

                String url = ServletUtils.normalizePath(req.getRequestURL().toString());
                String purchaseURL = url + "/" + purchase.getPurchaseID().toString();

                Map<String, String> headers = new HashMap(1);
                headers.put("Location", purchaseURL);

                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, JsonToRestPurchaseDtoConversor.toObjectNode(purchaseDto), headers);

            } catch (MatchNotAvailableException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toMatchNotAvaliableException(ex), null);
            } catch (NotEnoughTicketsException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toNotEnoughTicketsException(ex), null);
            }
        }

    }
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {
        ServletUtils.checkEmptyPath(req);
        String userEmail = req.getParameter("userEmail");

        List<Purchase> purchases = MatchServiceFactory.getService().findUserPurchases(userEmail);
        List<RestPurchaseDto> purchaseDtos = PurchaseToRestPurchaseDtoConversor.toRestPurchaseDto(purchases);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestPurchaseDtoConversor.toArrayNode(purchaseDtos), null);
    }
}
