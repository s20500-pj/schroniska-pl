import React from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

export default function LoggedInUser() {
    axios.defaults.withCredentials = true
    const navigate = useNavigate();
    const firstName = localStorage.getItem("firstName");
    const shelterName = localStorage.getItem("shelterName");
    const handleLogout = async () => {
        await axios.get("http://localhost:8080/api/auth/logout");
        localStorage.clear();
        navigate('/');
    };

    return (
        <div>
            <h2>Logged in user as {firstName}</h2>
            <p>{firstName ? <p>FirstName: {firstName}</p> : <p>ShelterName: {shelterName}</p>}</p>
            <button onClick={handleLogout}>Wyloguj</button>
        </div>
    )
}