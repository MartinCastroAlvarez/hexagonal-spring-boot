// Forms.tsx

import React, { useEffect, useState } from 'react';

import { useNavigate } from 'react-router-dom';
import { PATHS } from './Pages';
import {
  HexDate,
  HexForm,
  HexMiniButton,
  HexInput,
  HexButton,
  HexToolbar,
  HexSelect,
  HexTimeInput,
  HexProductInput,
  HexSpinner,
  HexUserInput
} from './Components';
import {
  faPlus,
  faUserPlus,
  faX,
  faFile,
  faMagnifyingGlass,
  faAngleRight,
  faAnglesRight,
  faAngleLeft,
  faAnglesLeft,
  faAngleDown,
  faAngleUp
} from '@fortawesome/free-solid-svg-icons';

export const HexLoginForm = ({ onSubmit }: { onSubmit: any }) => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  return (
    <HexForm>
      <HexInput placeholder="admin@test.com" type="email" value={email} onChange={(value: any) => setEmail(value)} />
      <HexInput placeholder="test" type="password" value={password} onChange={(value: any) => setPassword(value)} />
      <HexButton text="Submit" onClick={() => {
        console.log(`HexLoginForm.onSubmit: ${email} ${password}`);
        onSubmit({email, password})
      }} />
      <HexButton text="I do't have an account" onClick={() => navigate(PATHS.SIGNUP)} />
      <HexButton text="I don't remember my password" onClick={() => navigate(PATHS.RESET)} />
    </HexForm>
  );
};

export const HexSignUpForm = ({ onSubmit }: { onSubmit: any }) => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password1, setPassword1] = useState('');
  const [password2, setPassword2] = useState('');
  return (
    <HexForm>
      <HexInput placeholder="Email address" type="email" value={email} onChange={(value: any) => setEmail(value)} />
      <HexInput placeholder="Password"  type="password" value={password1} onChange={(value: any) => setPassword1(value)} />
      <HexInput placeholder="Re-enter your password"  type="password" value={password2} onChange={(value: any) => setPassword2(value)} />
      <HexButton text="Submit" onClick={() => {
        console.log(`HexSignUpForm.onSubmit: ${email} ${password1} ${password2}`);
        if (password1 === password2)
          onSubmit({email, password: password1});
      }} />
      <HexButton text="I already have an account" onClick={() => navigate(PATHS.LOGIN)} />
    </HexForm>
  );
};

export const HexResetForm = ({ onSubmit }: { onSubmit: any }) => {
  const [email, setEmail] = useState('');
  return (
    <HexForm>
      <HexInput placeholder="example@gmail.com" type="email" value={email} onChange={(value: any) => setEmail(value)} />
      <HexButton text="Submit" onClick={() => {
        console.log(`HexResetForm.onSubmit: ${email}`);
        onSubmit({email})
        setEmail('');
      }} />
    </HexForm>
  );
};

export const HexSearchForm = ({ onSubmit }: { onSubmit: any }) => {
  const [term, setTerm] = useState('');
  return (
    <HexForm>
      <HexToolbar>
        <HexInput placeholder="What are you looking for?" type="text" value={term} onChange={(value: any) => setTerm(value)} />
        <HexMiniButton icon={faMagnifyingGlass} onClick={() => {
          console.log(`HexSearchForm.onSubmit: ${term}`);
          onSubmit({term})
        }} />
      </HexToolbar>
    </HexForm>
  );
};

export const HexPaginationForm = ({
  isLoading,
  pagination,
  sortOptions
}: {
  isLoading: boolean,
  pagination: any,
  sortOptions: string[]
}) => {
  if (isLoading)
    return null;
  return (
    <HexForm>
      <HexToolbar>
        {pagination.page > 1 && (
          <HexMiniButton icon={faAnglesLeft} onClick={() => pagination.setPage(0)}/>
        )}
        {pagination.page > 0 && (
          <HexMiniButton icon={faAngleLeft} onClick={() => pagination.setPage(pagination.page - 1)}/>
        )}
        <HexMiniButton text={(pagination.page + 1).toString()} onClick={() => pagination.setPage(pagination.page)}/>
        <HexMiniButton icon={faAngleRight} onClick={() => pagination.setPage(pagination.page + 1)}/>
        <HexMiniButton icon={faAnglesRight} onClick={() => pagination.setPage(pagination.page + 10)}/>
      </HexToolbar>
      <HexToolbar>
        <HexSelect options={["5", "10", "20", "30"]} label="Limit" value={pagination.size.toString()} onChange={(value: string) => pagination.setSize(parseInt(value))} />
        <HexSelect options={sortOptions} label="Sort" value={pagination.sort} onChange={pagination.setSort} />
        <HexButton icon={pagination.asc ? faAngleDown : faAngleUp} text={pagination.asc ? 'Ascending': 'Descending'} onClick={() => pagination.setAsc(!pagination.asc)}/>
      </HexToolbar>
    </HexForm>
  );
};

export const HexDateForm = ({ start, end, onChange } : { start: Date, end: Date, onChange: any }) => {
  const [startDate, setStartDate] = useState(start);
  const [endDate, setEndDate] = useState(end);
  useEffect(() => {
    onChange(startDate, endDate)
  }, [startDate, endDate]);
  return (
    <HexForm>
      <HexToolbar>
        <HexDate value={startDate} onChange={(value: string) => {
          setStartDate(new Date(value + 'T00:00:00'));
        }} />
        <>&nbsp;</>
        <HexDate value={endDate} onChange={(value: string) => {
          setEndDate(new Date(value + 'T00:00:00'));
        }} />
      </HexToolbar>
    </HexForm>
  );
}

export const HexProductListForm = () => {
  const navigate = useNavigate();
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faPlus} text="Create Product" onClick={() => navigate(PATHS.PRODUCT_CREATE)} />
      </HexToolbar>
    </HexForm>
  );
}

export const HexSaleListForm = () => {
  const navigate = useNavigate();
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faPlus} text="Create Sale" onClick={() => navigate(PATHS.SALE_CREATE)} />
      </HexToolbar>
    </HexForm>
  );
}

export const HexPurchaseListForm = () => {
  const navigate = useNavigate();
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faPlus} text="Create Purchase" onClick={() => navigate(PATHS.PURCHASE_CREATE)} />
      </HexToolbar>
    </HexForm>
  );
}

export const HexMessageListForm = () => {
  const navigate = useNavigate();
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faPlus} text="Create Message" onClick={() => navigate(PATHS.MESSAGE_CREATE)} />
      </HexToolbar>
    </HexForm>
  );
}

export const HexMeetingListForm = () => {
  const navigate = useNavigate();
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faPlus} text="Create Meeting" onClick={() => navigate(PATHS.MEETING_CREATE)} />
      </HexToolbar>
    </HexForm>
  );
}

export const HexPtoListForm = () => {
  const navigate = useNavigate();
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faPlus} text="Create Time Off" onClick={() => navigate(PATHS.PTO_CREATE)} />
      </HexToolbar>
    </HexForm>
  );
}

export const HexScheduleListForm = () => {
  const navigate = useNavigate();
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faPlus} text="Create Schedule" onClick={() => navigate(PATHS.SCHEDULE_CREATE)} />
      </HexToolbar>
    </HexForm>
  );
}

export const HexWorkForm = ({ title, onCheckIn, onCheckOut, reload } : { title?:string, onCheckIn: any, onCheckOut: any, reload: any }) => {
  return (
    <HexForm title={title}>
      <HexToolbar>
        <HexButton text="Check-In" onClick={async () => { await onCheckIn(); reload() }}/>
        <HexButton text="Check-Out" onClick={async () => { await onCheckOut(); reload() }}/>
      </HexToolbar>
    </HexForm>
  );
}

export const HexUserDetailsForm = ({
  user,
  onActivate,
  onDeactivate,
  onPromoteManager,
  onPromoteSalesman,
  onPromoteProvider,
  onDemoteManager,
  onDemoteSalesman,
  onDemoteProvider,
  reload,
} : {
  user: any,
  onActivate: any,
  onDeactivate: any,
  onPromoteManager: any,
  onPromoteSalesman: any,
  onPromoteProvider: any,
  onDemoteManager: any,
  onDemoteSalesman: any,
  onDemoteProvider: any,
  reload: any
}) => {
  const navigate = useNavigate();
  if (!user || !user.id)
    return null;
  return (
    <HexForm>
      <HexToolbar>
        <HexButton text="Edit" onClick={() => navigate(PATHS.USER_EDIT.replace(':id', user.id))} />
        {user.isActive && <HexButton text="Deactivate" onClick={async () => { await onDeactivate(parseInt(user.id)); reload() }} />}
        {!user.isActive && <HexButton text="Activate" onClick={async () => { await onActivate(parseInt(user.id)); reload() }} />}
      </HexToolbar>
      <HexToolbar>
        {user.role === 'USER' && <HexButton text="Promote to Manager" onClick={async () => { await onPromoteManager(parseInt(user.id)); reload() }} />}
        {user.role === 'USER' && <HexButton text="Promote to Salesman" onClick={async () => { await onPromoteSalesman(parseInt(user.id)); reload() }} />}
        {user.role === 'USER' && <HexButton text="Promote to Provider" onClick={async () => { await onPromoteProvider(parseInt(user.id)); reload() }} />}
        {user.role === 'MANAGER' && <HexButton text="Demote" onClick={async () => { await onDemoteManager(parseInt(user.id)); reload() }} />}
        {user.role === 'SALESMAN' && <HexButton text="Demote" onClick={async () => { await onDemoteSalesman(parseInt(user.id)); reload() }} />}
        {user.role === 'PROVIDER' && <HexButton text="Demote" onClick={async () => { await onDemoteProvider(parseInt(user.id)); reload() }} />}
      </HexToolbar>
    </HexForm>
  )
}

export const HexProductDetailsForm = ({
  product,
  onDeactivate,
  reload 
}: {
  product: any,
  onDeactivate: any,
  reload: any
}) => {
  const navigate = useNavigate();
  if (!product || !product.id)
    return null;
  return (
    <HexForm>
      <HexToolbar>
        <HexButton text="Edit" onClick={() => navigate(PATHS.PRODUCT_EDIT.replace(':id', product.id))} />
        {product.isActive && <HexButton text="Deactivate" onClick={async () => { await onDeactivate(parseInt(product.id)); reload() }} />}
      </HexToolbar>
    </HexForm>
  )
}

export const HexMeetingDetailsForm = ({
  meeting,
  onDelete,
}: {
  meeting: any,
  onDelete: any,
}) => {
  const navigate = useNavigate();
  if (!meeting || !meeting.id)
    return null;
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faUserPlus} text="Invite" onClick={() => navigate(PATHS.INVITE.replace(':id', meeting.id))} />
        <HexButton icon={faFile} text="Attach" onClick={() => navigate(PATHS.ATTACH.replace(':id', meeting.id))} />
        {meeting.isActive && <HexButton text="Deactivate" onClick={async () => { await onDelete(parseInt(meeting.id)); navigate(PATHS.MEETINGS) }} />}
      </HexToolbar>
    </HexForm>
  )
}

export const HexMessageDetailsForm = ({
  message,
  onSend,
  onDelete,
}: {
  message: any,
  onSend: any,
  onDelete: any,
}) => {
  const navigate = useNavigate();
  if (!message || !message.id)
    return null;
  return (
    <HexForm>
      <HexToolbar>
        {!message.sentAt && <HexButton text="Send" onClick={() => { onSend(parseInt(message.id)); navigate(PATHS.MESSAGES) }} /> }
        <HexButton icon={faUserPlus} text="Add Recipient" onClick={() => navigate(PATHS.ADD_RECIPIENT.replace(':id', message.id))} />
        {message.isActive && <HexButton text="Deactivate" onClick={async () => { await onDelete(parseInt(message.id)); navigate(PATHS.MESSAGES) }} />}
          <HexButton text="Delete" icon={faPlus} onClick={async () => { await onDelete(parseInt(message.id)); navigate(PATHS.MESSAGES) }} />
      </HexToolbar>
    </HexForm>
  )
}

export const HexPtoDetailsForm = ({ pto, onDelete } : { pto: any, onDelete: any }) => {
  const navigate = useNavigate();
  if (!pto || !pto.id)
    return null;
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faX} text="Delete" onClick={async () => { await onDelete(parseInt(pto.userId), parseInt(pto.id)); navigate(PATHS.PTO) }} />
      </HexToolbar>
    </HexForm>
  )
}

export const HexScheduleDetailsForm = ({ schedule, onDelete } : { schedule: any, onDelete: any }) => {
  const navigate = useNavigate();
  if (!schedule || !schedule.id)
    return null;
  return (
    <HexForm>
      <HexToolbar>
        <HexButton icon={faX} text="Delete" onClick={async () => { await onDelete(parseInt(schedule.userId), parseInt(schedule.id)); navigate(PATHS.SCHEDULE) }} />
      </HexToolbar>
    </HexForm>
  )
}

export const HexCreateProductForm = ({ onSubmit } : { onSubmit: any }) => {
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  return (
    <HexForm>
      <HexInput type="text" placeholder="Product Name" value={name} onChange={(value: any) => setName(value)} />
      <HexInput type="number" placeholder="Price" min={0} max={100} value={price} onChange={(value: any) => setPrice(value)} />
      <HexButton text="Submit" onClick={() => onSubmit({ name, price: parseFloat(price) })} />
    </HexForm>
  )
}

export const HexCreatePtoForm = ({ onSubmit } : { onSubmit: any }) => {
  const [day, setDay] = useState(new Date);
  const [type, setType] = useState('PERSONAL');
  const [userId, setUserId] = useState('');
  return (
    <HexForm>
      <HexUserInput title="User ID" value={userId} onChange={setUserId}/>
      <HexDate value={day} onChange={(value: any) => setDay(value)} />
      <HexSelect label="Type" options={['PERSONAL', 'SICK', 'VACATION']} value={type} onChange={(value: any) => setType(value)} />
      <HexButton text="Submit" onClick={() => onSubmit({ userId, day, type })} />
    </HexForm>
  )
}

export const HexCreateScheduleForm = ({ onSubmit } : { onSubmit: any }) => {
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const [dayOfWeek, setDayOfWeek] = useState('');
  const [userId, setUserId] = useState('');
  return (
    <HexForm>
      <HexUserInput title="User ID" value={userId} onChange={setUserId}/>
      <HexSelect label="Week Day" options={['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY']} value={dayOfWeek} onChange={(value: any) => setDayOfWeek(value)} />
      <HexTimeInput title="Start" onChange={setStart} />
      <HexTimeInput title="End" onChange={setEnd} />
      <HexButton text="Submit" onClick={() => onSubmit({ userId, start, end, dayOfWeek })} />
    </HexForm>
  )
}

export const HexCreateMessageForm = ({ onSubmit } : { onSubmit: any }) => {
  const [subject, setSubject] = useState('');
  const [text, setText] = useState('');
  return (
    <HexForm>
      <HexInput type="text" placeholder="Subject" value={subject} onChange={(value: any) => setSubject(value)} />
      <HexInput type="text" placeholder="Message" value={text} onChange={(value: any) => setText(value)} />
      <HexButton text="Submit" onClick={() => onSubmit({ subject, text })} />
    </HexForm>
  )
}

export const HexCreateMeetingForm = ({ onSubmit } : { onSubmit: any }) => {
  const [title, setTitle] = useState('');
  const [day, setDay] = useState(new Date());
  return (
    <HexForm>
      <HexInput type="text" placeholder="Meeting Title" value={title} onChange={(value: any) => setTitle(value)} />
      <HexDate value={day} onChange={(value: any) => setDay(value)} />
      <HexButton text="Submit" onClick={() => onSubmit({ title, day })} />
    </HexForm>
  )
}

export const HexCreateSaleForm = ({ onSubmit } : { onSubmit: any }) => {
  const [productId, setProductId] = useState('');
  const [amount, setAmount] = useState('');
  const [price, setPrice] = useState('');
  return (
    <HexForm>
      <HexProductInput title="Product ID" value={productId} onChange={(value: any) => setProductId(value)} />
      <HexInput type="number" placeholder="Amount" min={0} max={100} value={amount} onChange={(value: any) => setAmount(value)} />
      <HexInput type="number" placeholder="Price" min={0} max={100} value={price} onChange={(value: any) => setPrice(value)} />
      <HexButton text="Submit" onClick={() => onSubmit({ productId: parseInt(productId), amount: parseFloat(amount), price: parseFloat(price) })} />
    </HexForm>
  );
};

export const HexCreatePurchaseForm = ({ onSubmit } : { onSubmit: any }) => {
  const [productId, setProductId] = useState('');
  const [amount, setAmount] = useState('');
  const [cost, setCost] = useState('');
  return (
    <HexForm>
      <HexProductInput title="Product ID" value={productId} onChange={setProductId}/>
      <HexInput type="number" placeholder="Amount" min={0} max={100} value={amount} onChange={(value: any) => setAmount(value)} />
      <HexInput type="number" placeholder="Price" min={0} max={100} value={cost} onChange={(value: any) => setCost(value)} />
      <HexButton text="Submit" onClick={() => onSubmit({ productId: parseInt(productId), amount: parseFloat(amount), cost: parseFloat(cost) })} />
    </HexForm>
  );
}

export const HexInviteUserToMessageForm = ({ messageId, onSubmit } : { messageId: number, onSubmit: any }) => {
  const [userId, setUserId] = useState('');
  return (
    <HexForm>
      <HexUserInput title="User ID" value={userId} onChange={setUserId}/>
      <HexButton text="Submit" onClick={() => onSubmit({ messageId, userId: parseInt(userId) })} />
    </HexForm>
  );
}

export const HexInviteUserToMeetingForm = ({ meetingId, onSubmit } : { meetingId: number, onSubmit: any }) => {
  const [userId, setUserId] = useState('');
  return (
    <HexForm>
      <HexUserInput title="User ID" value={userId} onChange={setUserId}/>
      <HexButton text="Submit" onClick={() => onSubmit({ meetingId, userId: parseInt(userId) })} />
    </HexForm>
  );
}

export const HexUpdateProductForm = ({ isLoading, product, onSubmit } : { isLoading: boolean, product: any, onSubmit: any }) => {
  const [name, setName] = useState(product.name);
  const [price, setPrice] = useState(product.price);
  if (isLoading)
    return <HexSpinner />
  return (
    <HexForm title="Update Product">
      <HexInput type="text" placeholder="Product Name" value={name} onChange={(value: any) => setName(value)} />
      <HexInput type="number" placeholder="Price" min={0} max={100} value={price} onChange={(value: any) => setPrice(value)} />
      <HexButton text="Submit" onClick={() => onSubmit({ id: product.id, name, price })} />
    </HexForm>
  );
}

export const HexUpdateUserForm = ({ isLoading, user, onSubmit } : { isLoading: boolean, user: any, onSubmit: any }) => {
  const [name, setName] = useState(user.name);
  if (isLoading)
    return <HexSpinner />
  return (
    <HexForm>
      <HexInput placeholder="Name" type="name" value={name} onChange={(value: any) => setName(value)} />
      <HexButton text="Submit" onClick={() => onSubmit({ id: user.id, name })} />
    </HexForm>
  );
}

export const HexUploadFileForm = ({ onSubmit }: { onSubmit: any }) => {
  const [file, setFile] = useState<File | null>(null);
  const [name, setName] = useState('');
  const handleFileChange = (file: any) => {
    setFile(file);
    setName(file.name);
  };
  return (
    <HexForm>
      <HexToolbar>
        <HexInput type="file" value={null} placeholder="Upload file" onChange={handleFileChange} />
        <HexButton text="Upload" onClick={() => {
          onSubmit({ file, name })
        }} />
      </HexToolbar>
    </HexForm>
  );
};