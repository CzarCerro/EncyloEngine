import React, { useState } from 'react';
import './styles/searchbar.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function SearchBar({ searchQuery, handleSearchInputChange, handleSearchResults }) {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);

    const [queryTypeIsDropped, setQueryTypeIsDropped] = useState(false);
    const [selectedOption, setSelectedOption] = useState('All');

    const queryTypeOptions = ['All', 'Title', 'Description']

    const toggleQueryTypeDropdown = () => {
        setQueryTypeIsDropped(!queryTypeIsDropped);
    };

    const handleQueryTypeOptionClick = (option) => {
        setSelectedOption(option);
        setQueryTypeIsDropped(false);
    };

    const handleSubmit = () => {
        if (searchQuery.trim() !== '') {
            setIsLoading(true);

            const queryTypeParameter = `queryType=${encodeURIComponent(selectedOption)}`;
            const searchQueryParameter = `query=${encodeURIComponent(searchQuery)}`;
            const url = `http://localhost:5000/search?${queryTypeParameter}&${searchQueryParameter}`;
        
            axios.get(url)
                .then(response => {
                    setIsLoading(false);
                    console.log(response.data);
                    handleSearchResults(response.data);
                    navigate('/searchResult');
                })
                .catch(error => {
                    setIsLoading(false);
                    console.error('An error occurred during search:', error);
                });
        }
    };

    return (
        <div className="searchBar">
            <div >
                <div className="input-group">
                    <div style={{ display: 'flex', justifyContent: 'row' }}>
                        <div className="form-outline">
                            <input
                                id="search-focus"
                                type="search"
                                className="form-control"
                                onChange={handleSearchInputChange}
                                placeholder="Enter your search query"
                                style={{
                                    width: '25rem',
                                    borderTopRightRadius: '0',
                                    borderBottomRightRadius: '0',
                                    borderTopLeftRadius: '2rem',
                                    borderBottomLeftRadius: '2rem',
                                    boxShadow: 'none',
                                }}
                                value={searchQuery}
                            />
                        </div>
                        <button
                            onClick={handleSubmit}
                            type="button"
                            className={`btn btn-primary ${isLoading ? 'loading-button' : ''
                                }`}
                            style={{
                                width: '5rem',
                                borderTopRightRadius: '2rem',
                                borderBottomRightRadius: '2rem',
                                borderTopLeftRadius: '0',
                                borderBottomLeftRadius: '0',
                            }}
                            disabled={isLoading}
                        >
                            <i className="fas fa-search"></i>
                        </button>
                    </div>
                </div>

            </div>
            <div className="dropdown">
                <button className='dropdown-toggle dropdownMenuItem' onClick={toggleQueryTypeDropdown}>
                    {selectedOption}
                </button>
                {
                    queryTypeIsDropped &&
                    <div className="dropdownMenu">
                        {queryTypeOptions.map((queryTypeOption) => (
                            <button
                                key={queryTypeOption}
                                className="dropdownMenuItem"
                                onClick={() => handleQueryTypeOptionClick(queryTypeOption)}
                            >
                                {queryTypeOption}
                            </button>
                        ))}
                    </div>
                }
            </div>
        </div>
    );
}

export default SearchBar;
