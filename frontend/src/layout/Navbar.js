import {Link} from "react-router-dom";
import logo from "./LogoSchroniska-pl.png";
import hero_img from "./hero_img.jpeg";
import React from "react";

export default function Navbar() {
    return (
        <div>
            <nav className="flex shadow-md rounded-2xl">
                <div className="align-top">
                    <img src={logo} className="w-64" alt="Logo schronisko.pl"/></div>
                    <div className="m-auto flex justify-between">
                    <button
                        className="px-10 py-2 m-5 border-2 border-orange rounded-2xl hover:bg-orange text-white active:bg-brown ">
                        <Link className="justify-center text-base text-center text-brown font-medium hover:text-white" to="/">Home</Link>
                    </button>
                        <button
                            className="px-10 py-2 m-5 border-2 border-orange rounded-2xl  hover:bg-orange text-white active:bg-brown ">
                            <Link className="justify-center text-base text-center text-brown font-medium	 " to="/adduser">Zarejestruj się</Link>
                        </button>
                        <button
                            className="px-10 py-2 m-5 border-2 border-orange rounded-2xl  hover:bg-orange text-white active:bg-brown ">
                            <Link className="py-15 justify-center text-base	 text-center text-brown font-medium	" to="/addshelter">Zarejestruj schronisko</Link>
                        </button>
                        <button
                            className="px-10 py-2 m-5 border-2 border-orange rounded-2xl  hover:bg-orange text-white active:bg-brown ">
                            <Link className="py-15 px-15 justify-center text-base text-center text-brown font-medium " to="/login">Zaloguj się</Link>
                        </button>
                    </div>
            </nav>
            <header>
                <div className="relative py-8">
                    <img src={hero_img} className="m-auto rounded-2xl" alt="Dogs in shelter"/>
                    <div className="w-50 h-100 backdrop-opacity-10 backdrop-invert bg-brown/60  rounded-2xl  absolute top-1/4 -right-1/4 -translate-x-1/2 -translate-y-1/2">
                        <h2 className="text-3xl text-white text-center p-10 indent-8 font-bold">Znajdź czworonoga i zaopiekuj się nim!</h2>
                    </div>
                    </div>
            </header>
        </div>
    )
}