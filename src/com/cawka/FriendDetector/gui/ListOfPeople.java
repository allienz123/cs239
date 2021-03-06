package com.cawka.FriendDetector.gui;

import com.cawka.FriendDetector.Main;
import com.cawka.FriendDetector.Person;
import com.cawka.FriendDetector.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListOfPeople extends ListView 
{
	protected static final int CONTEXTMENU_DELETEITEM = 0;
	private PeopleAdapter _adapter;
	private ImageWithFaces _picture;
	
	private Handler _handler=new Handler( );
	
	private Main _friendDetector;
	
	public ListOfPeople( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		
		init( );
	}

	public ListOfPeople( Context context, AttributeSet attrs ) 
	{
		super(context, attrs);
		
		init( );
	}

	public ListOfPeople( Context context ) 
	{
		super( context );
		
		init( );
	}
	
	public void setFriendDetector( Main friendDetector )
	{
		_friendDetector=friendDetector;
	}

	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	private void init( )
	{
		_adapter=new PeopleAdapter( getContext(), R.layout.row );
		setAdapter( _adapter );
		
////		setLongClickable( true );
//		setOnCreateContextMenuListener( new OnCreateContextMenuListener()
//			{
//				public void onCreateContextMenu( ContextMenu menu, View v,
//						ContextMenuInfo menuInfo ) 
//				{
//					super.onCreateContextMenu( menu, v, menuInfo );
//					  menu.add(0, EDIT_ID, 0, "Edit");
//					  menu.add(0, DELETE_ID, 0,  "Delete");
//
////					Toast.makeText( getContext(), "Stuff2", Toast.LENGTH_SHORT ).show( );
//					menu.setHeaderTitle("ContextMenu");
//	                menu.add(0, CONTEXTMENU_DELETEITEM,0, "Delete this favorite!"); 
//				}
//				
//			} );
//		
//		this.setOnLongClickListener( new OnLongClickListener()
//			{
//				public boolean onLongClick( View v ) 
//				{
//					Toast.makeText( getContext(), "Stuff", Toast.LENGTH_SHORT ).show( );
//					return false;
//				}
//			} );
	}
	
	public void onCreateContextMenu( final int position )
	{
		AlertDialog.Builder alert = new AlertDialog.Builder( getContext() );  
		  
		alert.setTitle( R.string.enter_name_of_the_person );  
		  
		final EditText input = new EditText( getContext() );  
		alert.setView( input );
		
		if( _adapter.getItem(position).hasName() )
			input.setText( _adapter.getItem(position).getName() );

		alert.setPositiveButton( "Save", new DialogInterface.OnClickListener() 
			{   
				public void onClick( DialogInterface dialog, int whichButton )
				{  
					String value=input.getText().toString();
					
					if( !value.equals("") ) 
					{
						_adapter.getItem( position ).setName( value );
						_adapter.notifyDataSetChanged( );
						
						ListOfPeople.this._friendDetector.onLearnRequest( _adapter.getItem(position).getFace(), value );
					}
				}  
			} );  
		  
		alert.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() 
			{  
				public void onClick( DialogInterface dialog, int whichButton ) 
				{
					// just do nothing  
				}  
			} );  
		  
		alert.show();		
	}
	
	public void refresh( )
	{
		_handler.post( 
			new Runnable()
			{
				public void run( )
				{
					_adapter.notifyDataSetChanged( );
				}
			} );
	}
	
	public void clear( )
	{
		_adapter.clear( );
		_adapter.notifyDataSetChanged( );
	}
	
	public void add( Person person )
	{
		_adapter.add( person );
		_adapter.notifyDataSetChanged( );
		_picture.invalidate( );
	}
	
	public void setImageWithFaces( ImageWithFaces picture )
	{
		_picture=picture;
	}
	
	public void setAdapter( ListAdapter adapter )
	{
		super.setAdapter( adapter );
		_adapter=(PeopleAdapter)adapter;
	}
	
	
	
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	private class PeopleAdapter extends ArrayAdapter<Person> 
    {
		public PeopleAdapter( Context context, int textViewResourceId )
		{
			super( context, textViewResourceId );
		}

		public View getView( int position, View convertView, ViewGroup parent )
		{
			View v=convertView;
			if( v==null ) 
			{
				LayoutInflater vi=(LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
				v=vi.inflate( R.layout.row, null );
			}
			
			Person person=getItem( position );
			
			TextView name=(TextView)v.findViewById( R.id.row_text );
			name.setText( person.getName() );
			name.setTextColor( person.getColor() );
			
			ImageView face=(ImageView)v.findViewById( R.id.row_image );
			face.setImageBitmap( person.getFace() );
			
			return v;
		}
	}
}
