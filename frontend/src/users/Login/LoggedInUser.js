import React from "react";
import {useNavigate} from "react-router-dom";

export default function LoggedInUser() {
    const navigate = useNavigate();
    const token = localStorage.getItem("authToken");
    const firstName = localStorage.getItem("firstName");
    const handleLogout = () => {
        localStorage.removeItem('authToken');
        navigate('/');
    };

    return (
        <div>
            <h2>Logged in user as {firstName}</h2>
            <p>Your JWT token is: {token ? <p>Token: {token}</p> : <p>No token found</p>}</p>
            <button onClick={handleLogout}>Wyloguj</button>
        </div>
    )
}