import React, {useState} from "react";
import axios from "axios";
import ShelterServerConstants from "../util/ShelterServerConstants";
import Messages from "../util/Messages";

export default function VirtualAdoptionBtn({isPerson, animal}) {

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



    if (!isPerson(userType) || !canBeAdopted(animal)) {
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

            axios
                .post(ShelterServerConstants.ADDRESS_SERVER_LOCAL + `/adoption/virtual/${animal.id}/${amount}`,
                    {
                        headers: {
                            'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                        }
                    }
                )
                .then(response => {
                    alert("Za chwilę zostaniesz przeniesiony na stronę PayU")
                    window.location.replace(response.data);
                })
                .catch(error => {
                    console.error(error);
                });
        }
    };

    const handleBack = (e) => {
        e.preventDefault();
        setShowForm(false);
    };

    return (
        <>
            {!showForm ? (
                <button
                    className="ml-2 text-brown font-bold py-2 px-4 m-5 bg-white border-orange text-brown hover:bg-orange border-2 rounded-2xl hover:scale-105"
                    onClick={showVirtualAdoptionForm}
                >
                    Adoptuj wirtualnie
                </button>
            ) : (
                <form onSubmit={(e) => handleVirtualAdoption(e)}
                      className="justify-center items-center flex overflow-x-hidden overflow-y-auto fixed inset-0 z-50">
                    <div className="relative w-auto my-6 mx-auto max-w-3xl">
                        <div
                            className="p-4 border-0 rounded-lg shadow-lg relative flex flex-col w-full bg-white outline-none focus:outline-none">
                            <h2 className="text-center text-xl text-orange font-bold p-5 h-fit">
                                Wirtualna adopcja </h2>
                            <p className="mb-4">{Messages.VIRTUAL_ADOPTION_INFORMATION_BTN}</p>
                            <div
                                className=" items-start justify-between p-5 border-solid border-orange-200 rounded-t">
                                <label className="block text-brown font-semibold ">Kwota [PLN]:</label>
                                <input type="number" value={amount}
                                       onChange={(e) => setAmount(e.target.value)} required
                                       className="w-full border-2 border-gray p-2 rounded-md focus:outline-orange"/>
                                {errorMessage && (
                                    <div className="text-red-500 font-semibold">{errorMessage}</div>
                                )}
                            </div>
                            <div className="flex justify-between">
                                <button type="button" onClick={(e) => handleBack(e)}
                                        className=" px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                    <p className="py-15 justify-center text-base text-center text-brown font-medium	">Anuluj</p>
                                </button>
                                <button type="submit"
                                        className=" px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                    <p className="py-15 justify-center text-base text-center text-brown font-medium	">  Zaadoptuj zwierzątko </p>
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            )
            }
        </>
    );


}

