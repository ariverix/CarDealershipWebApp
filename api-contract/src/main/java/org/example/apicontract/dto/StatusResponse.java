package org.example.apicontract.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class StatusResponse extends RepresentationModel<StatusResponse> {

    private final String status;
    private final String error;

    public StatusResponse(String status, String error) {
        this.status = status;
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        StatusResponse that = (StatusResponse) o;
        return Objects.equals(status, that.status) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status, error);
    }
}
