import React, {useEffect, useMemo, useState} from "react";
import axios from "axios";
export default function AnimalCard({data}) {
    axios.defaults.withCredentials = true;
    const [
        getTableProps,
        getTableBodyProps,
        headerGroups,
        page,
        nextPage,
        previousPage,
        canNextPage,
        canPreviousPage,
        prepareRow,
        pageIndex,
        pageOptions ] =useState("");


    return (
        <div className="">
            <div className=" flex flex-wrap max-h-40">
                {data.map(data => {
                    return (
                        <div className="m-4 flex items-start" key={data.id}>
                            <div className=" bg-white rounded-3xl shadow-xl overflow-hidden">
                                <div className="max-w-md mx-auto">
                                    <div className="bg-orange p-4 sm:p-6">
                                        <img src={data.photo} alt="Zdjecie zwierzęcia" className="w-15 h-fit"/>
                                        <p className=" text-[22px] font-bold">{data.name}</p>
                                        <div className="flex">
                                            <p className="font-bold text-gray-700 text-[16px] mb-1">{data.sex}</p>
                                        </div>
                                        <p className="font-[15px] mt-3">{data.information}</p>
                                        <button
                                            className="px-10 py-2 m-5 rounded-2xl bg-white text-white active:bg-brown hover:shadow-xl">
                                            <p className="py-15 justify-center text-base text-center text-brown font-medium">Więcej informacji</p>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )
                })
                }
            </div>
        </div>

    )
}
