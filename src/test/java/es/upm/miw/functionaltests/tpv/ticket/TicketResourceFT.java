package es.upm.miw.functionaltests.tpv.ticket;

import es.upm.miw.functionaltests.HttpRequestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TicketResourceFT {

    public static final String URL = "http://localhost:8080/tpv-ticket/tickets";
    @Autowired
    private HttpRequestBuilder httpRequestBuilder;

    @Test
    void testReadTicket() {
        ResponseEntity<Ticket> response = this.httpRequestBuilder
                .get(URL + "/{urlToken}", "AAAABBBBCCCCDDDDEEEE00").exchange(Ticket.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
