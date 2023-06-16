import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

function PopupDeleteAnimal({setOpenModal, id, animal, navigate,name}) {

    const deleteAnimal = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/animal/delete/${animal.id}`)
            navigate('/shelterAnimalList')
        } catch (error) {
            console.error(error);
        }
    };


    return (<>
            <div
                className="justify-center items-center flex overflow-x-hidden overflow-y-auto fixed inset-0 z-50
                outline-none focus:outline-none font-display">
                <div className="relative w-auto my-6 mx-auto max-w-3xl">
                    <div
                        className="border-0 rounded-lg shadow-lg relative flex flex-col w-full bg-white outline-none focus:outline-none">
                        <div
                            className="flex items-start justify-between p-5 border-b border-solid border-slate-200 rounded-t">
                            <h3 className="text-2xl font-semibold text-brown">
                                Alert </h3>
                            <button
                                className="p-1 ml-auto bg-transparent border-0 text-black opacity-5 float-right text-3xl leading-none font-semibold outline-none focus:outline-none"
                            >
                    <span
                        className="bg-transparent text-black opacity-5 h-6 w-6 text-2xl block outline-none focus:outline-none">
                      ×
                    </span>
                            </button>
                        </div>
                        <div className="relative p-6 flex-auto">
                            <p className="my-4 text-orange text-lg leading-relaxed">
                                Czy na pewno chcesz usunąć zwierze o imieniu {animal.name}?</p>
                        </div>
                        <div
                            className="flex items-center justify-end p-6 border-t border-solid border-slate-200 rounded-b">
                            <button
                                className="bg-orange text-white active:bg-orange font-display uppercase text-sm px-6 py-3 rounded-2xl shadow hover:shadow-lg outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                                type="button"
                                onClick={() =>deleteAnimal(id) }
                            >
                                TAK
                            </button>
                            <button
                                className="bg-orange text-white active:bg-orange font-display uppercase text-sm px-6 py-3 rounded-2xl shadow hover:shadow-lg outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                                type="button"
                                onClick={() =>setOpenModal(false) }
                            >
                                NIE
                            </button>

                        </div>
                    </div>
                </div>
            </div>
            <div className="opacity-25 fixed inset-0 z-40 bg-black"></div>
        </>
    );
}

export default PopupDeleteAnimal;
