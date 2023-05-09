import React from "react";

export default function ShelterPage() {

    const shelterName = localStorage.getItem("shelterName");
    return (
        <div className="flex">
            <section className="w-full bg-cover bg-center py-48 bg-background-hero">
                <div className="container mx-auto text-center text-white">
                    <h1 className="text-5xl text-white mb-10 font-display font-bold">Witaj schronisko {shelterName}</h1>
                    <p className="text-xl mb-10 font-display">Zarządzaj schroniskiem i adopcjami!</p>
                    <a href="/shelterAnimalList"
                       className="font-display bg-orange text-white py-4 px-12 rounded-full hover:bg-brown hover:text-orange">Pokaż
                        zwierzęta</a>
                </div>
            </section>
        </div>
    )
}