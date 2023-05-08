//todo this is simple workaround. need to integrate react with spring.
import React from "react";
import {CheckCircleIcon} from "@heroicons/react/20/solid";

export default function PayUConfirmationPage() {
    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none ">
            <div className="px-10 font-display bg-white bg-opacity-90 text-center">
            <div className="mx-auto pt-32 h-48 w-24 rounded-full">
                <CheckCircleIcon className="text-orange"/>
            </div>
            <div className="m-4">
                <div className="m-auto text-center p-5 h-48">
                    <h2 className="text-center text-2xl text-orange font-bold p-5">Dziękujemy za dokonanie wpłaty.</h2>
                    <p>   Teraz oczekujemy na zatwierdzenie płatności przez wybrane schronisko.
                          Po potwierdzeniu płatności, otrzymasz e-mail z potwierdzeniem.</p>
                </div>
                <button type="submit"
                        className="px-10 py-2 m-10 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                    <a
                        href="/userVirtualAdoptionList"
                        className=' justify-center text-base text-center text-brown font-medium	'
                    >
                        Powrót </a>
                </button>
            </div>
        </div>
        </div>
    );
}