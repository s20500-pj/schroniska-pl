import React from "react";

export default function Hero() {
    return (
        <div className=" bg-gray-50 flex items-center ">
            <section className="w-full bg-cover bg-center py-48 bg-background-hero">
                <div className="container mx-auto text-center text-white">
                    <h1 className="text-5xl mb-10 font-display font-bold">Schroniska.pl</h1>
                    <p className="text-xl mb-10 font-display">Znajdź i zaadpotuj czworonoga.</p>
                    <a href="/animalList"
                       className="font-display bg-orange text-white py-4 px-12 rounded-full hover:bg-brown hover:text-orange">Pokaż
                        zwierzęta</a>
                </div>
            </section>
        </div>
    )
}