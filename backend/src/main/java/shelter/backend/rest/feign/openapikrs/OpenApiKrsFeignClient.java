package shelter.backend.rest.feign.openapikrs;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shelter.backend.rest.feign.openapikrs.req.Rejestr;
import shelter.backend.rest.feign.openapikrs.res.OpenApiKrsResponse;

@FeignClient(name = "${client.feign.openapikrs.name}", url = "${client.feign.openapikrs.url}")
public interface OpenApiKrsFeignClient {

    @GetMapping(value = "/OdpisAktualny/{krs}", produces = MediaType.APPLICATION_JSON_VALUE)
    OpenApiKrsResponse getActualWriteOff(@PathVariable String krs, @RequestParam(name = "rejestr") Rejestr rejestr);
}
