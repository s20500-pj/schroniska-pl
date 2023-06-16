import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link, useNavigate, useParams} from "react-router-dom";
import ShelterServerConstants from "../../util/ShelterServerConstants";
import {SHELTER_APPROVAL_STATUS_OPTIONS} from "../../util/Enums";
import PopupDeleteShelterByShelter from "./PopupDeleteShelterByShelter";

export default function ShelterDetails() {
    axios.defaults.withCredentials = true;
    const {id} = useParams();
    const [shelter, setShelter] = useState();
    const [error, setError] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [clientId, setClientId] = useState('');
    const [clientSecret, setClientSecret] = useState('');
    const [merchantPosId, setMerchantPosId] = useState('');
    const [modalOpen, setModalOpen] = useState(false);
    const userType = localStorage.getItem("userType");

    useEffect(() => {
        axios.get(ShelterServerConstants.ADDRESS_SERVER_LOCAL + `/shelter/${id}`)
            .then((response) => {
                setShelter(response.data);
            })
            .catch((error) => {
                console.error("Error fetching user data:", error);
                setError(error);
            });
    }, [id]);


    const handleAproveShelter = () => {
        setShowForm(true);
    };

    const handleBack = (e) => {
        e.preventDefault();
        setShowForm(false);
    };

    const handleFinishRegistration = (e) => {
        e.preventDefault();
        const data = {
            clientId: clientId,
            clientSecret: clientSecret,
            merchantPosId: merchantPosId,
            shelterId: shelter.id
        };
        axios.post(ShelterServerConstants.ADDRESS_SERVER_LOCAL + '/shelter/enable', JSON.stringify(data),
            {
                headers: {
                    'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                }
            })
            .then(response => {
                if (!response.data.disabled) {
                    alert("Schronisko zatwierdzone. Email z potwierdzeniem został wysłany do schorniska");
                    setShelter(response.data)
                } else {
                    alert("Problem z zatwierdzeniem schroniska");
                    console.log(response);
                }
            })
            .catch(error => {
                console.error(error);
            });
    };


    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    function handleSubmit(e) {
        e.preventDefault();
        axios
            .put(`http://localhost:8080/shelter/update`, JSON.stringify(shelter), {
                headers: {
                    'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                }
            })
            .then((response) => {
                setShelter(response.data);
            })
            .catch((error) => {
                console.error("Error update user data:", error);
                setError(error);
            });
    }


    const handleInput = (e) => {
        setShelter({...shelter, [e.target.name]: e.target.value});
        const {name, value} = e.target;
        setShelter((prevState) => ({
            ...prevState,
            address: {
                ...prevState.address,
                [name]: value,
            },
        }));
    };
    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className=" px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-5 h-fit ">
                    Dane schroniska
                </h2>
                <div className='block text-center pb-5 '>
                    {shelter ? (
                        <>
                            <div className=''>
                                <div className="">
                                    <p className=' text-center text-3xl font-bold text-brown pb-5'>Schronisko:</p>
                                    <p className=' text-center text-5xl font-bold text-orange pb-5'>{shelter.shelterName}</p>
                                    {userType === "ADMIN" && !shelter.disabled ? (
                                        <p className="text-center font-bold text-orange-700">ZATWIERDZONE</p>) : null}
                                    <div className="  flex justify-center">
                                        <div className="text-left px-8">
                                            <p className='font-bold pt-2 text-left'>Adres schroniska: </p>
                                            <p className=' pt-2'>Ulica: {shelter.address.street}{" "} </p>
                                            <p className=' pt-2'>Numer
                                                budynku: {shelter.address.buildingNumber}{" "} </p>
                                            <p className=' pt-2'>Numer
                                                mieszkania: {shelter.address.flatNumber}{" "} </p>
                                            <p className="pt-2">Kod pocztowy: {shelter.address.postalCode.slice(0, 2)}-{shelter.address.postalCode.slice(2)}{" "}</p>
                                            <p className=' pt-2'>Miejscowość: {shelter.address.city}</p>
                                            <p className=' pt-2'>Numer KRS: {shelter.address.krsNumber}</p>
                                        </div>
                                        <div className='block text-left px-8'>
                                            <p className='font-bold pt-2 text-left'>Kontakt do schroniska: </p>
                                            <p className=' pt-2'>Telefon: {shelter.address.phone}</p>
                                            <p className=' pt-2'>E-mail: {shelter.email}</p>
                                            {/*{userType !== "SHELTER" ? (*/}
                                            {/*    <Link to={`/shelteranimalListUnlogged`}>*/}
                                            {/*        <button type="submit"*/}
                                            {/*                className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">*/}
                                            {/*            <p className="py-15 justify-center text-base text-center text-brown font-medium	">Pokaż*/}
                                            {/*                zwierzęta*/}
                                            {/*            </p>*/}
                                            {/*        </button>*/}
                                            {/*    </Link> ):(null)}*/}
                                            {userType === "SHELTER" ? (
                                                <Link to={`/shelteranimalList`}>
                                                <button type="submit"
                                                        className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                                    <p className="py-15 justify-center text-base text-center text-brown font-medium	">Pokaż
                                                        zwierzęta
                                                    </p>
                                                </button>
                                            </Link> ):(null)}
                                        </div>
                                        <div>
                                            {userType === "SHELTER" ? (
                                                <div className="flex">
                                                    <div>
                                                        <h2 className=" text-md text-orange font-bold h-fit pb-5">
                                                            Tutaj możesz zaktualizować numer telefonu.</h2>
                                                        <form onSubmit={handleSubmit}>
                                                            <div className="">
                                                                <input
                                                                    type={"text"}
                                                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                                                    name="phone"
                                                                    defaultValue={shelter.address && shelter.address.phone}
                                                                    onChange={handleInput}
                                                                />
                                                            </div>
                                                            <div className="m-auto text-center">
                                                                <button type="submit"
                                                                        className=" px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                                                    <p className="py-15 justify-center text-base text-center text-brown font-medium	">Aktualizuj
                                                                        dane</p>
                                                                </button>
                                                            </div>
                                                            {modalOpen && <PopupDeleteShelterByShelter setOpenModal={setModalOpen} setLoading={setLoading} navigate={navigate} id={id} shelter={shelter}/>}
                                                            <button
                                                                type="button"
                                                                onClick={() => setModalOpen(true)}
                                                                className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white hover:bg-orange text-white active:bg-brown"
                                                            >
                                                                <p className="py-15 justify-center text-base text-center text-brown font-medium">
                                                                    Usuń shronisko
                                                                </p>
                                                            </button>
                                                        </form>
                                                    </div>
                                                </div>
                                            ) : (<></>)
                                            }
                                        </div>
                                    </div>
                                </div>
                            </div>
                            {shelter.disabled && shelter.approvalStatus !== SHELTER_APPROVAL_STATUS_OPTIONS.COMPLETED && userType === "ADMIN" && (
                                !showForm ? (
                                    <button className="bg-orange text-white font-bold py-3 mt-3 px-4 rounded-2xl"
                                            onClick={handleAproveShelter}>Zatwierdź schronisko</button>
                                ) : (
                                    <form onSubmit={(e) => handleFinishRegistration(e)}
                                          className="bg-white mt-4 p-4 rounded-md shadow-orange shadow-2xl max-w-4xl mx-auto">
                                        <p className="text-lg font-semibold mb-4">Przed zatwierdzeniem schroniska
                                            należy
                                            je zarejestrować w panelu menadżera serwisu <a
                                                href="https://merch-prod.snd.payu.com/pl/standard/user/login"
                                                target="_blank" rel="noopener noreferrer"
                                                className="text-blue-700 underline"> PayU</a>.</p>
                                        Po rejestracji, podaj poniższe dane schroniska zarejestrowanego w PayU.
                                        <p>Numer konta bankowego schroniska (IBAN): <b>{shelter.iban}</b></p>
                                        <p className="italic text-xs text-red-500"> * po zatwierzedniu schroniska,
                                            numer
                                            IBAN jest usuwany *</p>
                                        <div className="mb-4">
                                            <label className="block text-gray-700 font-semibold mt-3 mb-2">Client
                                                ID:</label>
                                            <input type="text" value={clientId}
                                                   onChange={(e) => setClientId(e.target.value)} required
                                                   className="w-full border border-gray-300 p-2 rounded-md"/>
                                        </div>
                                        <div className="mb-4">
                                            <label className="block text-gray-700 font-semibold mb-2">Client
                                                Secret:</label>
                                            <input type="text" value={clientSecret}
                                                   onChange={(e) => setClientSecret(e.target.value)} required
                                                   className="w-full border border-gray-300 p-2 rounded-md"/>
                                        </div>
                                        <div className="mb-4">
                                            <label className="block text-gray-700 font-semibold mb-2">Merchant POS
                                                ID:</label>
                                            <input type="text" value={merchantPosId} required
                                                   onChange={(e) => setMerchantPosId(e.target.value)}
                                                   className="w-full border border-gray-300 p-2 rounded-md"/>
                                        </div>
                                        <div className="flex justify-between">
                                            <button type="button" onClick={(e) => handleBack(e)}
                                                    className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white hover:bg-orange text-white active:bg-brown ">
                                                <p className="py-15 justify-center text-base text-center text-brown font-medium	">
                                                    Anuluj </p>
                                            </button>
                                            <button type="submit"
                                                    className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white hover:bg-orange text-white active:bg-brown ">
                                                <p className="py-15 justify-center text-base text-center text-brown font-medium	">
                                                    Zakończ rejestrację schroniska
                                                </p>
                                            </button>
                                        </div>
                                    </form>
                                ))}
                        </>
                    ) : (
                        <p>Ładowanie danych schroniska...</p>
                    )}
                </div>
            </div>
        </div>
    );
}





