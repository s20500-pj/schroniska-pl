import React from "react";
import {useTable, usePagination} from "react-table";
import axios from "axios";
import icon from  '../dog-cat-icon.jpeg';

export default function Table({columns, data}) {
    axios.defaults.withCredentials = true;
    const placeholderImage = icon;
    const onImageError = (e) => {
        e.target.src = placeholderImage
    }
    const {
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
        pageOptions,
        state: {pageSize}
    } = useTable(
        {
            columns,
            data,
            initialState: {pageIndex: 0, pageSize: 10}
        },
        usePagination
    );

    const handleNextPage = () => {
        nextPage();
    };

    const handlePreviousPage = () => {
        previousPage();
    };

    return (
        <>
            <div className="overflow-auto p-5">
            <table {...getTableProps()} className=" w-full divide-y divide-gray-300 border-2 border-grey-400 rounded-xl">
                <thead className="mt-1 rounded-md border-gray-500 shadow-sm">
                {headerGroups.map((headerGroup) => (
                    <tr {...headerGroup.getHeaderGroupProps()} >
                        {headerGroup.headers.map((column) => (
                            <th {...column.getHeaderProps()} className="px-6 py-3 text-s font-bold text-center text-orange uppercase tracking-wide">{column.render("Header")}</th>
                        ))}
                    </tr>
                ))}
                </thead>
                <tbody {...getTableBodyProps()} className="bg-white divide-y divide-gray-200">
                {page.map((row, i) => {
                    prepareRow(row);
                    return (
                        <tr {...row.getRowProps()} className="p-3">
                            {row.cells.map((cell, j) => {
                                if (cell.column.Header === "Zdjęcie") {
                                    return (
                                        <td {...cell.getCellProps() }className='p-5'>
                                            <img src={cell.value ? cell.value : placeholderImage}
                                                 onError={onImageError}
                                                 className=" shadow-xl border-2 border-orange rounded object-cover h-32 w-48"/>
                                        </td>
                                    );
                                } else {
                                    return (
                                        <td {...cell.getCellProps()} className="p-1 text-center text-brown" >{cell.render("Cell")}</td>
                                    );
                                }
                            })}
                        </tr>
                    );
                })}
                </tbody>
            </table>
            <div className="flex justify-center">
                <button onClick={handlePreviousPage} disabled={!canPreviousPage} className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                    <p className="py-15 justify-center text-base text-center text-brown font-medium	">Poprzednia strona</p>
                </button>
                <button onClick={handleNextPage} disabled={!canNextPage} className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                    <p className="py-15 justify-center text-base text-center text-brown font-medium	">Następna strona</p>
                </button>
                {pageIndex !== undefined && pageOptions !== undefined ? (
                    <div>
                        Page{" "}
                        <strong>
                            {pageIndex + 1} of {pageOptions.length}
                        </strong>
                    </div>
                ) : null}
            </div>
            </div>
        </>
    );
}
