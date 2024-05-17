// Components.tsx

import React, { useCallback } from 'react';

import { Api } from './Api';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faFaceSadTear,
  faClock,
  faSpinner,
  faAngleDown,
  faCube,
  faUser
} from '@fortawesome/free-solid-svg-icons';

import { useLoader, String } from './Utils';

export const HexPage = ({ title, children }: { title?: string, children: React.ReactNode }) => {
  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      { title && <HexTitle text={title} /> }
      {children}
    </div>
  );
}

export const HexLink = ({ text, path, icon }: { icon?: any, text?: string, path: string }) => {
    return <Link to={ path }>
      { icon && <FontAwesomeIcon icon={icon} className="ml-4 mr-1 align-middle" /> }
      { text }
    </Link>
}

export const HexTitle = ({ text }: { text: string }) => {
  return (
    <h1 className="text-xl text-gray-500 border-b border-gray-300 p-8" style={{ textAlign: 'left', paddingLeft: '0', }}>
      { text }
    </h1>
  );
}

export const HexSubTitle = ({ text }: { text: string }) => {
  return (
    <h5 className="text-xl py-3 text-gray-500" style={{ fontSize: '1em', textAlign: 'left' }}>
      { text }
    </h5>
  );
}

export const HexForm = ({ title , children }: { title?: string, children: React.ReactNode }) => {
  return (
    <form className="flex flex-col py-4">
      <div className="space-y-6 w-full max-w-lg py-2">
      { title && <HexSubTitle text={title} /> }
      { children }
      </div>
    </form>
  );
}

export const HexInput = ({ icon, type, value, placeholder, onChange, min, max}: { icon?: any, min?: number, max?: number, type: string, placeholder: string, value: any, onChange: any }) => {
  return (
    <HexToolbar>
      { icon && <FontAwesomeIcon icon={icon} className="py-3 text-lg text-gray-500 align-middle" /> }
      <div className="relative w-full">
        { value && <label className="absolute -top-3 left-0 text-xs text-gray-400">{placeholder}</label> }
        <input
          type={type}
          value={value}
          placeholder={placeholder}
          onChange={(e: any) => { 
            if (type == 'file') {
              if (e.target.files.length > 0)
                onChange(e.target.files[0]);
            } else if (type == 'number') {
              let value = e.target.value;
              if (value !== null) {
                if (type === 'number') value = parseInt(e.target.value, 10);
                if (min !== undefined && value < min) value = min;
                if (max !== undefined && value > max) value = max;
              }
              onChange(value);
            } else
              onChange(e.target.value);
          }}
          className="mt-1 block w-full px-4 py-2 border border-gray-300 shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500 text-xs"
        />
      </div>
    </HexToolbar>
  );
}

export const HexMiniButton = ({ text, icon, onClick }: { text?: string, icon?: any, onClick: () => void }) => {
  return (
    <HexButton text={text} onClick={onClick} icon={icon} style={{ maxWidth: '50px' }} />
  );
}

export const HexButton = ({ text, icon, onClick, style }: { text?: string, icon?: any, onClick: () => void, style?: object }) => {
  return <button
    type="button"
    onClick={() => {
      console.log(`HexButton.onClick: ${text}`);
      onClick()
    }}
    style={{ maxWidth: '300px', ...style }}
    className="w-full flex items-center justify-center py-1 px-4 border border-transparent shadow-sm text-xs font-small text-white bg-blue-500 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
      { icon && <FontAwesomeIcon icon={icon} className="align-middle"/> }
      { icon && text && <>&nbsp;</> }
      { text }
  </button>;
}

export const HexSelect = ({ options, label, value, onChange }: { options: string[], label: string, value: string, onChange: any }) => {
  const [isOpen, setIsOpen] = useState(false);
  const toggleDropdown = () => setIsOpen(!isOpen);
  const handleOptionClick = (option: string) => {
    console.log(`HexSelect.handleOptionClick: ${option}`);
    setIsOpen(false);
    onChange(option);
  };
  return (
    <div className="relative w-full" style={{ maxWidth: '200px' }}>
      <HexButton icon={faAngleDown} text={`${label}: ${value}`} onClick={() => {
        console.log(`HexSelect.onClick: ${label}`);
        toggleDropdown()
      }}/>
      {isOpen && (
        <div className="absolute mt-1 w-full shadow-lg bg-white ring-1 ring-black ring-opacity-5">
          <ul className="py-1 text-gray-700">
            {options.map(option => (
              <li key={option}
                  className="px-4 py-2 text-sm hover:bg-gray-100 cursor-pointer"
                  onClick={() => {
                    console.log(`HexSelect.onClick: ${option}`);
                    handleOptionClick(option);
                  }}>
                {option}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export const HexSpinner = () => {
  return <div className="flex justify-center items-center p-16">
    <FontAwesomeIcon icon={faSpinner} className="text-4xl text-gray-500 animate-spin" />
  </div>
}

export const HexEmpty = () => {
  return (
    <div className="flex flex-col bg-white justify-center items-center p-32">
      <FontAwesomeIcon icon={faFaceSadTear} className="text-4xl text-gray-500" />
      <HexSubTitle text="No results" />
    </div>
  );
};

export const HexList = ({ title, data, isLoading, columns, onClick }: { title?: string, data: any[], isLoading: boolean, columns: string[], onClick: any }) => {
  if (data === null || data === undefined)
    return <>
      { title && <HexSubTitle text={title} /> }
      <HexSpinner/>
    </>
  if (isLoading)
    return <>
      { title && <HexSubTitle text={title} /> }
      <HexSpinner/>
    </>
  if (data.length === 0)
    return <>
      { title && <HexSubTitle text={title} /> }
      <HexEmpty/>
    </>
  return (
    <>
      { title && <HexSubTitle text={title} /> }
      <div className="overflow-x-auto border-b border-t border-gray-300">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              {columns.map((column, index) => (
                <th key={index} scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  {column}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {data.map((item, idx) => (
              <tr key={idx} className="cursor-pointer hover:bg-gray-100">
                {columns.map((column, colIndex) => (
                  <td key={colIndex} className="px-6 py-4 whitespace-nowrap text-xs text-gray-500" onClick={() => {
                    console.log(`HexList.onClick: ${item[column]}`);
                    onClick(item);
                  }}>
                    <String value={item[column]} />
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

export const HexTable = ({ title, data, isLoading, onClick }: { title?: string, data: object, isLoading: boolean, onClick: any }) => {
  if (data === null || data === undefined)
    return <>
      { title && <HexSubTitle text={title} /> }
      <HexSpinner/>
    </>
  if (isLoading)
    return <>
      { title && <HexSubTitle text={title} /> }
      <HexSpinner/>
    </>
  return (
    <>
      { title && <HexSubTitle text={title} /> }
      <div className="flex flex-col w-full">
          <ul className="divide-y divide-gray-200 text-xs border-b border-t border-gray-300">
              {Object.entries(data).map(([key, value], index) => (
                  <li key={index} className="flex justify-between p-2 cursor-pointer text-gray-500 bg-white" onClick={() => {
                    console.log(`HexTable.onClick: ${key} - ${value}`);
                    onClick(key, data);
                  }}>
                      <span className="text-gray-500">{key}</span>
                      <span><String value={value}/></span>
                  </li>
              ))}
          </ul>
      </div>
    </>
  );
};

export const HexToolbar = ({ children }: { children: React.ReactNode }) => {
  return (
    <div className="w-full flex flex-col items-center">
      <div className="flex justify-center gap-4 w-full" style={{ maxWidth: '500px' }}>
        { children }
      </div>
    </div>
  );
}

const HexIdInput = ({ icon, title, value, onChange }: { icon?: any, title: string, value?: string, onChange: any }) => {
  return (
    <HexInput icon={icon} type="number" placeholder={title} min={0} value={value || ''} onChange={(value: any) => {
      if (value === null || value === undefined || value === '')
        onChange(null);
      else
        onChange((parseInt(value)).toString())
    }} />
  )
}

export const HexDate = ({ value, onChange } : { value: Date, onChange: any }) => {
  if (value === null || value === undefined) value = new Date()
  if (typeof value === 'string') value = new Date(value);
  return (
    <HexInput icon={faClock} type="date" placeholder="Date" value={value.toISOString().slice(0, 10)} onChange={(value: any) => onChange(value)} />
  );
}

export const HexCard = ({ title, subtitle } : { title: string, subtitle: string }) => {
  if (!title)
    return null;
  return (
    <div className="flex flex-col items-center p-4 bg-white text-gray-800 border border-gray-300">
      <div className="flex flex-col items-center">
        <h5 className="text-lg text-gray-700">{title}</h5>
        <h6 className="text-sm text-gray-500">{subtitle}</h6>
      </div>
    </div>
  );
}

export const HexUserInput = ({ title, value, onChange }: { title: string, value?: string, onChange: any }) => {
  const [userId, setUserId] = useState(null);
  const [user] = useLoader(async () => {
    if (!userId || userId === '')
      return {};
    return await Api.getUser(userId);
  }, [userId])
  return <>
    <HexCard title={user?.name} subtitle={user?.role} />
    <HexIdInput icon={faUser} title={title} value={value} onChange={(value: number) => {
      setUserId(value as any);
      onChange(value);
    }}/>
  </>
}

export const HexProductInput = ({ title, value, onChange }: { title: string, value?: string, onChange: any }) => {
  const [productId, setProductId] = useState(null);
  const [product] = useLoader(async () => {
    if (!productId || productId === '')
      return {};
    return await Api.getProduct(productId);
  }, [productId])
  return <>
    <HexCard title={product?.name} subtitle={'$' + product?.price} />
    <HexIdInput icon={faCube} title={title} value={value} onChange={(value: number) => {
      setProductId(value as any);
      onChange(value);
    }}/>
  </>
}

export const HexTimeInput = ({ title, onChange }: { title: string, onChange: any }) => {
  const [hour, setHour] = useState('');
  const [min, setMin] = useState('');
  const onHourChange = useCallback((value: any) => {
    setHour(value);
    onChange(`${value}:${min}:00`);
  }, [hour, min]);
  const onMinChange = useCallback((value: any) => {
    setMin(value);
    onChange(`${hour}:${value}:00`);
  }, [hour, min]);
  return (
    <HexToolbar>
      <FontAwesomeIcon icon={faClock} className="py-3 text-lg text-gray-500 align-middle" />
      <HexInput type="number" placeholder={`${title} hour`} min={0} max={23} value={hour} onChange={(value: any) => onHourChange(value)} />
      <HexInput type="number" placeholder={`${title} minute`} min={0} max={59} value={min} onChange={(value: any) => onMinChange(value)} />
    </HexToolbar>
  );
}