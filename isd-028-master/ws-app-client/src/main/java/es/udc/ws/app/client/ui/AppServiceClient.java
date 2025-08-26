package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientMatchService;
import es.udc.ws.app.client.service.ClientMatchServiceFactory;
import es.udc.ws.app.client.service.dto.ClientMatchDto;
import es.udc.ws.app.client.service.dto.ClientPurchaseDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectedException;
import es.udc.ws.app.client.service.exceptions.ClientIncorrectBankCardException;
import es.udc.ws.app.client.service.exceptions.ClientMatchNotAvailableException;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughTicketsException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) throws InterruptedException {

        if (args.length == 0) {
            printUsageAndExit();
        }

        ClientMatchService clientMatchService =
                ClientMatchServiceFactory.getService();
        if ("-a".equalsIgnoreCase((args[0]))) {
            validateArgs(args, 5, new int[]{2, 3});

            // [addMatch] MatchServiceClient -a <matchDate> <ticketsPrice> <capacity> <visitingTeam>

            try {
                Long matchId = clientMatchService.addMatch(new ClientMatchDto(null,
                        LocalDateTime.parse(args[1]), Double.parseDouble(args[2]),
                        Integer.parseInt(args[3]), args[4]));

                System.out.println("Match " + matchId + " created succesfully");

            } catch (NumberFormatException | InputValidationException ex) {
                System.out.println(ex.getClass().getName() + ":" + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-f".equalsIgnoreCase((args[0]))) {
            validateArgs(args, 2, new int[]{1});

            // [findMatch] AppServiceClient -f <matchId>

            try {
                ClientMatchDto matchDto = clientMatchService.findMatch(Long.parseLong(args[1]));
                System.out.println("Found Match:");
                System.out.println("Id: " + matchDto.getMatchId() +
                        ", MatchDate: " + matchDto.getMatchDate() +
                        ", TicketsPrice: " + matchDto.getTicketsPrice() +
                        ", Capacity: " + matchDto.getCapacity() +
                        ", AvaliableTickets: " + (matchDto.getCapacity() - matchDto.getSoldUnits()) +
                        ", VisitingTeam: " + matchDto.getVisitingTeam());

            } catch (InstanceNotFoundException ex) {
                System.out.println(ex.getClass().getName() + ":" + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-fd".equalsIgnoreCase((args[0]))) {
            validateArgs(args, 2, new int[]{});

            // [findMatches] AppServiceClient -fd <endDate>

            try {
                List<ClientMatchDto> matchDtos = clientMatchService.findMatches(LocalDateTime.parse(args[1] + "T00:00:00"));
                System.out.println("Found " + matchDtos.size() +
                        " match(es) between now: " + LocalDateTime.now().toString().substring(0, 10) +
                        " and: " + args[1]);

                for (ClientMatchDto matchDto : matchDtos) {
                    System.out.println("Id: " + matchDto.getMatchId() +
                            ", MatchDate: " + matchDto.getMatchDate() +
                            ", TicketsPrice: " + matchDto.getTicketsPrice() +
                            ", Capacity: " + matchDto.getCapacity() +
                            ", AvaliableTickets: " + (matchDto.getCapacity() - matchDto.getSoldUnits()) +
                            ", VisitingTeam: " + matchDto.getVisitingTeam());
                }
            } catch (InputValidationException ex) {
                System.out.println(ex.getClass().getName() + ":" + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if ("-b".equalsIgnoreCase(args[0])) {
            validateArgs(args,5,new int[] {3,4});

            // [buyTickets] AppServiceClient -b <userEmail> <creditCardNumber> <numTickets> <matchId>

            try {
                ClientPurchaseDto purchaseDto = clientMatchService.buyTickets(args[1],args[2],Integer.parseInt(args[3]),Long.parseLong(args[4]));

                System.out.println("Succesfully purchase tickets to match " + args[4] +
                        " with purchase number "  + purchaseDto.getPurchaseId());

            } catch (InputValidationException | InstanceNotFoundException | ClientMatchNotAvailableException |
                     ClientNotEnoughTicketsException ex) {
                System.out.println(ex.getClass().getName() + ":" + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if ("-fp".equalsIgnoreCase(args[0])) {
            validateArgs(args,2,new int[] {});

            //[findPurchases] AppServiceClient -fp <userEmail>

            try {
                List<ClientPurchaseDto> purchaseDtos = clientMatchService.findUserPurchases(args[1]);

                System.out.println("Found " + purchaseDtos.size() +
                        " purchase(s) from user: " + args[1]);

                for (ClientPurchaseDto purchaseDto: purchaseDtos) {
                    System.out.println("Id: " + purchaseDto.getPurchaseId() +
                            ", UserEmail: " + purchaseDto.getUserEmail() +
                            ", CreditCardNumber: " + purchaseDto.getBankCard() +
                            ", PurchaseDate: " + purchaseDto.getPurchaseDate() +
                            ", MatchId: " + purchaseDto.getMatchId() +
                            ", Units: " + purchaseDto.getUnits() +
                            ", Collected: "  + purchaseDto.getCollected());
                }
            } catch (InputValidationException ex) {
                System.out.println(ex.getClass().getName() + ":" + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-c".equalsIgnoreCase(args[0])) {
            validateArgs(args,3,new int[] {1});

            // [collectedTicket] AppServiceClient -c <purchaseId> <creditCardNumber>

            try {
                clientMatchService.collectedTicket(Long.parseLong(args[1]),args[2]);

                System.out.println("Tickets from purchase with purchaseID: " +
                        args[1] +  " collected");
            } catch (InputValidationException | InstanceNotFoundException |
                     ClientIncorrectBankCardException | ClientAlreadyCollectedException ex){
                System.out.println(ex.getClass().getName() + ":" + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {

        if(expectedArgs != args.length) {
            printUsageAndExit();
        }

        for(int i = 0; i < numericArguments.length; i++) {
            int position = numericArguments[i];

            try {
                Double.parseDouble(args[position]);
            } catch (NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                " [addMatch] AppServiceClient -a <matchDate> <ticketsPrice> <capacity> <visitingTeam>\n" +
                " [findMatch] AppServiceClient -f <matchId>\n" +
                " [findMatches] AppServiceClient -fd <endDate>\n" +
                " [buyTickets] AppServiceClient -b <userEmail> <creditCardNumber> <numTickets> <matchId>\n" +
                " [findPurchases] AppServiceClient -fp <userEmail>\n" +
                " [collectedTicket] AppServiceClient -c <purchaseId> <creditCardNumber>\n");
    }
}