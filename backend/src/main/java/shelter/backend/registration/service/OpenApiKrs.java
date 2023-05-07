package shelter.backend.registration.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import shelter.backend.rest.feign.openapikrs.OpenApiKrsFeignClient;
import shelter.backend.rest.feign.openapikrs.req.Rejestr;
import shelter.backend.rest.feign.openapikrs.res.OpenApiKrsResponse;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.utils.converter.CharsNormalizer;

@Service
@Slf4j
public class OpenApiKrs implements ApprovalProvider {

    private final OpenApiKrsFeignClient openApiKrsFeignClient;

    public OpenApiKrs(OpenApiKrsFeignClient openApiKrsFeignClient) {
        this.openApiKrsFeignClient = openApiKrsFeignClient;
    }

    @Override
    public boolean validateShelterDetails(String krs, String companyName) {
        if (krs == null) {
            return false;
        }

        OpenApiKrsResponse response;

        try {
            //TODO: If there will be more Rejestrs consider @Rertry, @Recover provided by Spring
            response = openApiKrsFeignClient.getActualWriteOff(krs, Rejestr.P);
        } catch (FeignException.FeignClientException.NotFound e) {
            log.info("Shelter details not found for KRS: {}, Rejestr: {}", krs, Rejestr.P);
            log.info("Trying for Rejestr: {}", Rejestr.S);
            response = tryForRejestrS(krs);
        }
        if (response == null) {
            return false;
        }

        return checkShelterDetails(companyName, response);
    }

    private OpenApiKrsResponse tryForRejestrS(String krs) {
        try {
            return openApiKrsFeignClient.getActualWriteOff(krs, Rejestr.S);
        } catch (FeignException.FeignClientException.NotFound e) {
            log.info("Shelter details for KRS: {} not found in OpenApiKrs provider", krs);
            return null;
        }
    }

    private boolean checkShelterDetails(String companyName, OpenApiKrsResponse response) {
        String companyNameResponse = CharsNormalizer.convertToEngChars(response.getOdpis().getDane().getDzial1().getDanePodmiotu().getNazwa().trim());
        if (StringUtils.containsIgnoreCase(companyNameResponse, companyName)) {
            return true;
        }
        log.info("Shelter details are not valid. CompanyName registered: {}, ResponseFromKrs: {}", companyName, companyNameResponse);
        return false;
    }
}
