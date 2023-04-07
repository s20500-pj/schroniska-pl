import React from "react";
import axios from "axios";

export default function LoggedInUser() {
    axios.defaults.withCredentials = true
    const firstName = localStorage.getItem("firstName");
    const shelterName = localStorage.getItem("shelterName");

    return (
        <div>
            <h2>Logged in user as {firstName}</h2>
            <p>{firstName ? <p>FirstName: {firstName}</p> : <p>ShelterName: {shelterName}</p>}</p>

        </div>
    )
}