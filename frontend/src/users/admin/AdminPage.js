import React from "react";

export default function AdminPage() {
    const firstName = localStorage.getItem("firstName");
    const lastName = localStorage.getItem("lastName");


    return (
        <div className="flex">
            <section className="w-full bg-cover bg-center py-48 bg-background-hero">
                <div className="container mx-auto text-center text-white">
                    <h1 className="text-5xl text-white mb-10 font-display font-bold">Witaj Adminie: {firstName} {lastName}</h1>
                    <a href="/userList"
                       className="m-3 font-display bg-orange text-white py-4 px-12 rounded-full hover:bg-brown hover:text-orange">Lista użytkowników
                        </a>
                    <a href="/shelterList"
                       className="m-3 font-display bg-orange text-white py-4 px-12 rounded-full hover:bg-brown hover:text-orange">Lista
                        schronisk</a>
                </div>
            </section>
        </div>
    )
}