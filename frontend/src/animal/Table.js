import React from "react";
import {useTable, usePagination} from "react-table";
import axios from "axios";

export default function Table({columns, data}) {
    axios.defaults.withCredentials = true;

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
            <table {...getTableProps()}>
                <thead>
                {headerGroups.map((headerGroup) => (
                    <tr {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map((column) => (
                            <th {...column.getHeaderProps()}>{column.render("Header")}</th>
                        ))}
                    </tr>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {page.map((row, i) => {
                    prepareRow(row);
                    return (
                        <tr {...row.getRowProps()}>
                            {row.cells.map((cell, j) => {
                                if (cell.column.Header === "Zdjęcie") {
                                    return (
                                        <td {...cell.getCellProps()}>
                                            <img src={cell.value} alt=""/>
                                        </td>
                                    );
                                } else {
                                    return (
                                        <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                                    );
                                }
                            })}
                        </tr>
                    );
                })}
                </tbody>
            </table>
            <div>
                <button onClick={handlePreviousPage} disabled={!canPreviousPage}>
                    Previous Page
                </button>
                <button onClick={handleNextPage} disabled={!canNextPage}>
                    Next Page
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
        </>
    );
}
