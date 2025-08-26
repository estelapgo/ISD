namespace java es.udc.ws.app.thrift

struct ThriftMatchDto {
    1: i64 matchId
    2: string matchDate
    3: double ticketsPrice
    4: i32 capacity
    5: i32 soldUnits
    6: string visitingTeam
}

struct ThriftPurchaseDto {
    1: i64 purchaseId
    2: string userEmail
    3: string bankCard
    4: string purchaseDate
    5: i64 matchId
    6: i32 units
    7: bool collected
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftMatchNotAvaliableException {
    1: string matchDate
}

exception ThriftNotEnoughTicketsException {
    1: i64 matchId
    2: i32 ticketsAvaliable
    3: i32 requestedTickets
}

service ThriftMatchService {
    ThriftMatchDto addMatch(1: ThriftMatchDto matchDto) throws (1: ThriftInputValidationException e)
    list<ThriftMatchDto> findMatches (1: string initDate, 2: string endDate) throws (1: ThriftInputValidationException e)
    ThriftMatchDto findMatch (1: i64 matchId) throws (1: ThriftInstanceNotFoundException e)
    ThriftPurchaseDto buyTickets (1: string userEmail, 2: string creditCardNumber, 3: i32 numTickets, i64 matchId) throws (1:ThriftInputValidationException e,
        2:ThriftInstanceNotFoundException e1, 3:ThriftMatchNotAvaliableException e2, 4:ThriftNotEnoughTicketsException e3)
}