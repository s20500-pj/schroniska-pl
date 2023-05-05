import React, {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import {AGE_OPTIONS, ANIMAL_STATUS_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import icon from '../dog-cat-icon.jpeg';
import ShelterServerConstants from "../util/ShelterServerConstants";
import Messages from "../util/Messages";

export default function VirtualAdoptionBtn({setAnimal, isPerson, animal}) {

    const [showForm, setShowForm] = useState(false);
    const [amount, setAmount] = useState('');
    const [errorMessage, setErrorMessage] = useState("");

    const userType = localStorage.getItem("userType");
    const userId = localStorage.getItem("userId");

    const canBeAdopted = (animal) => {
        return animal.animalStatus !== 'UNKNOWN' &&
            animal.animalStatus !== 'ADOPTED' &&
            animal.animalStatus !== 'DEAD';
    }

    const alreadyVirtuallyAdopted = (animal) => {
        return animal.adoptions.some(
            (adoption) =>
                adoption.user.id === parseInt(userId) &&
                adoption.adoptionType === 'VIRTUAL'
        );
    }

    if (!isPerson(userType) && !canBeAdopted(animal)) {
        return null;
    }

    const showVirtualAdoptionForm = () => {
        setShowForm(true);
    };

    const handleVirtualAdoption = (e) => {
        e.preventDefault();
        if (amount < 30) {
            setErrorMessage("Kwota musi być wynosić conamniej 30 PLN");
        } else {
            setErrorMessage("");
        }
        axios
            .post(ShelterServerConstants.ADDRESS_SERVER_LOCAL + `/adoption/virtual/${animal.id}/${amount}`,
                {
                    headers: {
                        'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                    }
                })
            .then(response => {
                if (response.status === 302) {
                    window.location.replace(response.headers.location);
                } else {
                    alert("Problem z adopcją wirtualną");
                    console.log(response);
                }
            })
            .catch(error => {
                console.error(error);
            });
    };

    const handleBack = (e) => {
        e.preventDefault();
        setShowForm(false);
    };

    return (
        <>
            {alreadyVirtuallyAdopted(animal) && (<p>Adoptowałeś już to zwierzę wirtualnie</p>)}
            {!showForm ? (
                <button
                    className="bg-orange ml-2 text-white font-bold py-2 px-4 rounded"
                    onClick={showVirtualAdoptionForm}
                >
                    Adoptuj wirtualnie
                </button>
            ) : (
                <form onSubmit={(e) => handleVirtualAdoption(e)}
                      className="bg-white mt-4 p-4 rounded-md shadow-orange shadow-2xl max-w-4xl mx-auto">
                    <p className="mb-4">{Messages.VIRTUAL_ADOPTION_INFORMATION_BTN}</p>
                    <div className="mb-4">
                        <label className="block text-gray-700 font-semibold mb-2">Kwota [PLN]:</label>
                        <input type="number" value={amount}
                               onChange={(e) => setAmount(e.target.value)} required
                               className="w-full border border-gray-300 p-2 rounded-md"/>
                        {errorMessage && (
                            <div className="text-red-500">{errorMessage}</div>
                        )}
                    </div>
                    <div className="flex justify-between">
                        <button type="button" onClick={(e) => handleBack(e)}
                                className="bg-gray-300 hover:bg-gray-400 text-gray-700 font-semibold py-2 px-4 rounded-md">Anuluj
                        </button>
                        <button type="submit"
                                className="bg-green hover:bg-purple-600 text-white font-semibold py-2 px-4 rounded-md">
                            Zaadoptuj zwierzątko
                        </button>
                    </div>
                </form>
            )
            }
        </>
    );


}

