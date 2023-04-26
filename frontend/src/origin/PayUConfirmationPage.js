import React, {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";

export default function PayUConfirmationPage() {
    axios.defaults.withCredentials = true;
    const {id} = useParams();
    const [shelter, setShelter] = useState(null);
    const [error, setError] = useState(null);


    useEffect(() => {
        axios.get(`http://localhost:8080/shelter/${id}`)
            .then((response) => {
                console.log(response.data);
                setShelter(response.data);
            })
            .catch((error) => {
                console.error("Error fetching user data:", error);
                setError(error);
            });
    }, [id]);

    if (error) {
        return <div>Error fetching user data: {error.message}</div>;
    }

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
                                    <div className="  flex justify-center">
                                        <div className="text-left px-8">
                                            <p className='font-bold pt-2 text-left'>Adres schroniska: </p>
                                            <p className=' pt-2'>Ulica: {shelter.address.street}{" "} </p>
                                            <p className=' pt-2'>Numer
                                                budynku: {shelter.address.buildingNumber}{" "} </p>
                                            <p className=' pt-2'>Numer
                                                mieszkania: {shelter.address.flatNumber}{" "} </p>
                                            <p className=' pt-2'>Kod pocztowy: {shelter.address.postalCode}{" "} </p>
                                            <p className=' pt-2'>Miejscowość: {shelter.address.city}</p>
                                            <p className=' pt-2'>Numer KRS: {shelter.address.krsNumber}</p>
                                        </div>
                                        <div className='block text-left px-8'>
                                            <p className='font-bold pt-2 text-left'>Kontakt do schroniska: </p>
                                            <p className=' pt-2'>Telefon: {shelter.address.phone}</p>
                                            <p className=' pt-2'>E-mail: {shelter.email}</p>
                                        </div>
                                    </div>


                                </div>
                            </div>

                        </>
                    ) : (
                        <p>Ładowanie danych schroniska...</p>
                    )}

                </div>

            </div>
        </div>
    );
}



