import React from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import UserNavigationBar from "../Panels/User/UserNavigationBar";

export default function LoggedInUser() {
    axios.defaults.withCredentials = true
    const navigate = useNavigate();
    const firstName = localStorage.getItem("firstName");
    const shelterName = localStorage.getItem("shelterName");
    const handleLogout = async () => {
        await axios.get("http://localhost:8080/auth/logout");
        localStorage.clear();
        navigate('/');
    };

    return (
        <div>
            <div>
                <div>
                    <h2 className="text-center text-2xl text-orange font-bold p-10">Witaj, jesteś zalogowany jako: {firstName}</h2>
                    <p>{firstName ? <p>FirstName: {firstName}</p> : <p>ShelterName: {shelterName}</p>}</p>


                </div>
            </div>
        </div>
    )
}