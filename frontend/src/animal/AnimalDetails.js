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
            <div className=" px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-10 h-fit">
                    Szczegóły zwierzaka
                </h2>
                <div className='lg:flex justify-around md:block'>
                    {animal ? (
                        <>
                            <div className=''>
                                <h2 className=' text-2xl font-bold text-brown'>Cześć jestem</h2>
                                <p className=' text-5xl font-bold text-orange'>{animal.name}</p>
                                <img src={(animal.imagePath)} alt="Zdjęcie"/>

                            </div>
                            <div className='flex 1/3 py-10'>
                                <div className=''>
                                    <p className='font-bold pt-2'>Gatunek: </p><p>{SPECIES_OPTIONS[animal.species]}</p>
                                    <p className='font-bold pt-2'>Płeć: </p><p>{SEX_OPTIONS[animal.sex]}</p>
                                    <p className='font-bold pt-2'>Wiek: </p><p>{AGE_OPTIONS[animal.age]}</p>
                                    <p className='font-bold pt-2'>Data urodzenia: </p><p>{animal.birthDate}</p>
                                    <p className='font-bold pt-2'>Status:</p><p>{STATUS_OPTIONS[animal.animalStatus]}</p>
                                    <p className='font-bold pt-2'>Dodatkowe informacje:</p><p> {animal.information}</p>
                                </div>
                                <div>
                                    <p className='font-bold pt-2'>Schronisko:</p><p> {animal.shelter.shelterName}</p>
                                    <p className='font-bold pt-2'>
                                        Adres schroniska: </p><p>{animal.shelter.address.street}{" "}
                                    {animal.shelter.address.buildingNumber}{" "}
                                    {animal.shelter.address.flatNumber}{" "}
                                    {animal.shelter.address.postalCode}{" "}
                                    {animal.shelter.address.city}
                                </p>
                                    <p className='font-bold pt-2'>Numer KRS: </p><p>{animal.shelter.address.krsNumber}</p>
                                    <p className='font-bold pt-2'>Telefon do schroniska:</p><p> {animal.shelter.address.phone}</p>
                                    <div className='flex justify-end py-10'>
                                        {animal && canAdopt(animal) && (
                                            <button
                                                className="bg-orange text-white font-bold py-2 px-4 rounded"
                                                onClick={() => handleAdoption(animal.id)}
                                            >
                                                Adoptuj
                                            </button>
                                        )}
                                    </div>

                                </div>
                            </div>

                        </>
                    ) : (
                        <p>Ładowanie danych zwierzaka...</p>
                    )}

                </div>

            </div>
        </div>
    );
}


