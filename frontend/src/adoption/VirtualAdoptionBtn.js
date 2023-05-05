import React, {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import {AGE_OPTIONS, ANIMAL_STATUS_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import icon from '../dog-cat-icon.jpeg';
import ShelterServerConstants from "../util/ShelterServerConstants";
import Messages from "../util/Messages";

export default function VirtualAdoptionBtn({setAnimal, isPerson, animal}) {

    const [showForm, setShowForm] = useState(false);

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
            ) : (<div>"TODO"</div>)
            }
        </>
    );


}

