import React from "react";
import axios from "axios";
import Hero from "../../layout/Hero";

export default function PersonPage() {
    axios.defaults.withCredentials = true;

    const firstName = localStorage.getItem("firstName");
    const lastName = localStorage.getItem("lastName");


    return (
        <div className="flex">
                <section className="w-full bg-cover bg-center py-48 bg-background-hero">
                    <div className="container mx-auto text-center text-white">
                    <h1 className="text-5xl text-white mb-10 font-display font-bold">Witaj {firstName} {lastName}</h1>
                        <p className="text-xl mb-10 font-display">Dziękujemy, że jesteś z nami!</p>
                        <a href="/animalList"
                           className="font-display bg-orange text-white py-4 px-12 rounded-full hover:bg-brown hover:text-orange">Pokaż
                            zwierzęta</a>
                    </div>
                 </section>
        </div>
    )
}