import React, {useState} from "react";
import axios from "axios";
import ReactPaginate from "react-paginate";
import {Link} from "react-router-dom";
import {AGE_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import icon from  '../dog-cat-icon.jpeg';
export default function AnimalCard({data, rename}) {
    axios.defaults.withCredentials = true;
    const placeholderImage = icon;
    const userType = localStorage.getItem("userType");
    const onImageError = (e) => {
        e.target.src = placeholderImage
    }

    const [pageNumber, setPageNumber] = useState(0);
    const animalsPerPage = 12;
    const pagesVisited = pageNumber * animalsPerPage;
    const displayAnimals = data
        .slice(pagesVisited, pagesVisited + animalsPerPage)
        .map(data => {
            return (
                <div className="p-5 flex" key={data.id}>
                    <div className="bg-orange rounded-3xl shadow-xl overflow-hidden hover:scale-105 ">
                        <div className="w-[220px] ">
                            <Link to={`/animalDetails/${data.id}`}>
                                <img src={data.imagePath ? data.imagePath : placeholderImage}
                                     onError={onImageError}
                                     alt="Zdjecie zwierzęcia"
                                     className="object-cover h-48 w-64"/>
                            </Link>
                            <div className="bg-orange p-4 w-30 sm:p-6 ">
                                <p className="text-[18px] font-medium">{SPECIES_OPTIONS[data.species]}</p>
                                <p className="text-[22px] font-bold">{data.name}</p>
                                <div className="flex">
                                    <p className="font-medium text-brown text-[16px] mb-1">{SEX_OPTIONS[data.sex]}</p>
                                </div>
                                <p className="font-[15px]">{AGE_OPTIONS[data.age]}</p>
                            </div>
                        </div>
                    </div>
                </div>
            )
        })
    const pageCount = Math.ceil(data.length / animalsPerPage);
    const changePage = ({selected}) => {
        setPageNumber(selected);
    };

    return (
        <div className="md:h-fit sm:h-fit p-5 ">
            {(userType === "SHELTER" ) ? <h3 className="font-display text-center text-brown font-bold text-2xl">Zwierzęta w schronisku</h3>
                : <h3 className="font-display text-center text-brown font-bold text-2xl">Zwierzęta w schroniskach</h3> }
            <div className="flex flex-wrap h-fit">
                {displayAnimals}
            </div>
            <ReactPaginate
                previousLabel={<button
                    className="px-10 py-2  border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                    <p className="py-15 justify-center text-base text-center text-brown font-medium	">Poprzednia
                        strona</p>
                </button>
                }
                nextLabel={<button
                    className="px-10 py-2 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                    <p className="py-15 justify-center text-base text-center text-brown font-medium	">Następna
                        strona</p>
                </button>}
                pageCount={pageCount
                }
                pageClassName="block bg-white border-orange text-brown hover:bg-orange border-2 rounded-2xl m-3 p-3"
                onPageChange={changePage}
                containerClassName={"flex items-center justify-center mt-8"}
                previousLinkClassName={"previousBttn"}
                nextLinkClassName={"nextBttn"}
                disabledClassName={"paginationDisabled"}
                activeClassName={"bg-orange"}

            />
        </div>
    )
}
