import React from "react";
import axios from "axios";
import ShelterAnimalList from "../../animal/ShelterAnimalList";

function PersonSettings() {
    axios.defaults.withCredentials = true;
    return (
        <div className="p-5">
            <h2>Twoje dane</h2>
        </div>
    )
}
export default PersonSettings;