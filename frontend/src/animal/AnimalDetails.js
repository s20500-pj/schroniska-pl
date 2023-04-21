import React, {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import {AGE_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS, STATUS_OPTIONS} from "../util/Enums";

export default function AnimalDetails() {
    const {id} = useParams();
    const [animal, setAnimal] = useState(null);
    const [reload, setReload] = useState(false);

    function canAdopt(animal) {
        const userType = localStorage.getItem("userType");
        const userId = localStorage.getItem("userId");

        if (userType !== "PERSON") {
            return false;
        }

        const hasValidRealAdoption = animal.adoptions.some(
            (adoption) =>
                adoption.user.id === parseInt(userId) &&
                adoption.adoptionType === 'REAL'

            /*&& new Date(adoption.validUntil) >= new Date()*/
        );
        return !hasValidRealAdoption && animal.animalStatus === 'READY_FOR_ADOPTION';
    }

    function handleAdoption(animalId) {
        axios
            .post(`http://localhost:8080/adoption/real/${animalId}`)
            .then((response) => {
                alert("Prośba o adopcję wysłana do schroniska!");
                setReload((prevReload) => !prevReload);
            })
            .catch((error) => {
                console.error("Error sending adoption request:", error);
                alert("Error sending adoption request. Please try again.");
            });
    }

    useEffect(() => {
        axios.defaults.withCredentials = true;
        axios
            .get(`http://localhost:8080/animal/${id}`)
            .then((response) => setAnimal(response.data))
            .catch((error) => console.error("Error fetching animal data:", error));
    }, [id, reload]);

    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className="px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-10">
                    Szczegóły zwierzaka
                </h2>
                {animal ? (
                    <>
                        <img src={animal.imagePath} alt=""/>
                        <p>Imię: {animal.name}</p>

                        <p>Gatunek: {SPECIES_OPTIONS[animal.species]}</p>
                        <p>Płeć: {SEX_OPTIONS[animal.sex]}</p>
                        <p>Wiek: {AGE_OPTIONS[animal.age]}</p>
                        <p>Data urodzenia: {animal.birthDate}</p>
                        <p>Status: {STATUS_OPTIONS[animal.animalStatus]}</p>
                        <p>Dodatkowe informacje: {animal.information}</p>
                        <p>Schronisko: {animal.shelter.shelterName}</p>
                        <p>
                            Adres schroniska: {animal.shelter.address.street}{" "}
                            {animal.shelter.address.buildingNumber}{" "}
                            {animal.shelter.address.flatNumber}{" "}
                            {animal.shelter.address.postalCode}{" "}
                            {animal.shelter.address.city}
                        </p>
                        <p>Numer KRS: {animal.shelter.address.krsNumber}</p>
                        <p>Telefon do schroniska: {animal.shelter.address.phone}</p>

                        {animal && canAdopt(animal) && (
                            <button
                                className="bg-orange text-white font-bold py-2 px-4 rounded"
                                onClick={() => handleAdoption(animal.id)}
                            >
                                Adoptuj
                            </button>
                        )}
                    </>
                ) : (
                    <p>Ładowanie danych zwierzaka...</p>
                )}
            </div>
        </div>
    );
}
