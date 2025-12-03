package com.antonia.uaicamping.utils;

import android.os.Bundle;

import com.antonia.uaicamping.data.model.Area;

public interface OnFragmentInteractionListener {
    void onNextStep(Bundle dataBundle);

    void onFinishCadastro(Area finalArea);

}
