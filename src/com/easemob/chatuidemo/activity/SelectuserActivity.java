/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.Config.Config;
import com.example.frag2.R;
import com.example.frag_activity.Event_send;
import com.easemob.chatuidemo.domain.User;

public class SelectuserActivity extends PickContactNoCheckboxActivity {
	private User selectUser;
	private String forward_msg_id;
	private String is_work;

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//forward_msg_id = getIntent().getStringExtra("forward_msg_id");
		//is_work=getIntent().getStringExtra("is_work");
	}
	
	
	

	@Override
	protected void onListItemClick(int position) {
//		if (position != 0) {
			selectUser = contactAdapter.getItem(position);
			Intent intent = new Intent(SelectuserActivity.this, AlertDialog.class);
			intent.putExtra("cancel", true);
			intent.putExtra("titleIsCancel", true);
			intent.putExtra("msg", "确认选择该用户  "+ selectUser.getUsername());
			startActivityForResult(intent, 1);
//		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			//Intent intent = new Intent(this, Event_send.class);
			if (selectUser == null)
				return;
			// it is single chat
			//intent.putExtra("userId", selectUser.getUsername());
			//intent.putExtra("forward_msg_id", forward_msg_id);
			//intent.putExtra("is_work", is_work);
			//startActivity(intent);
			Config.username=selectUser.getUsername();
			finish();

		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
