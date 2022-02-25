import React, { useState } from "react";
import "./SearchBar.css";
import Search from "@mui/icons-material/Search";
import Mic from "@mui/icons-material/Mic";
import { useNavigate } from "react-router";
import { useStateValue } from "../StateProvider";
import { actionTypes } from "../reducer";
import { Button } from "@mui/material";

function SearchBar({ hideButtons = false }) {
    const [values, dispatch] = useStateValue();

    // Catch the input from search bar
    const [input, setInput] = useState("");
    const navigate = useNavigate();

    const search = (event) => {
        event.preventDefault();
        dispatch({
            type: actionTypes.SET_SEARCH_TERM,
            term: input,
        });
        navigate("/search");
    };

    const lucky = (event) => {
        event.preventDefault();
        navigate("/lucky");
    };

    return (
        <form className="search">
            <div className="search__input">
                <Search className="search__inputIcon" />
                <input
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                />
                <Mic />
            </div>

            {!hideButtons ? (
                <div className="search__buttons">
                    <Button type="submit" onClick={search} variant="outlined">
                        Search
                    </Button>
                    <Button type="submit" onClick={lucky} variant="outlined">
                        I'm Feeling Doge
                    </Button>
                </div>
            ) : (
                <div className="search__buttons_2">
                    <Button
                        className="search__buttonsHidden"
                        type="submit"
                        onClick={search}
                    >
                        ℬ
                    </Button>
                </div>
            )}
        </form>
    );
}

export default SearchBar;
