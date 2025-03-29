package com.cy.solarapp;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("realtime")
    Call<List<RealTimeData>> getRealTimeData();
}
