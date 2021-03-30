#include<bits/stdc++.h>

using namespace std;


vector<vector<int>>pairs = {{0,1},{0,2},{1,2}};



struct data
{
	vector<int>left={3,3,3};
	vector<int>right={0,0,0};
	int turn=1;
};
vector<struct data> result;


void printres(struct data items)
{
	cout<<"Begin state"<<endl;
	printf("%d \t\t %d\n%d \t\t %d\n%d \t\t %d\n",items.left[0],items.right[0],items.left[1],items.right[1],items.left[2],items.right[2]);
	cout<<"End state"<<endl;
}


void dfs(struct data items, vector<vector<vector<int>>>&visited, vector<struct data> temp)
{
	if(items.turn==-1&&items.left[0]+items.left[1]+items.left[2]==0)
	{
		//cout<<"Goal state "<<temp.size()<<" "<<result.size()<<endl;
		if(temp.size()>result.size())
		{
			//cout<<"AAAAAAAAAAAAAA"<<endl;
			result.clear();
			for(int i=0;i<temp.size();i++)
			{
				result.push_back(temp[i]);
			}
		}
	}
	
	if(visited[items.left[0]][items.left[1]][items.left[2]])
	{
		//cout<<"State Visited before"<<endl;
		return;
	}
	///printres(items);
	visited[items.left[0]][items.left[1]][items.left[2]]=1;
	
	
	if(items.turn==1){
		items.turn = -1;
		for(auto item:pairs)
		{
			if(items.left[item[0]]>0&&items.left[item[1]]>0)
			{
				items.left[item[0]]--;items.right[item[0]]++;
				items.left[item[1]]--;items.right[item[1]]++;
				temp.push_back(items);
				//cout<<"I will tell you the size of temp: "<<result.size()<<endl;
				dfs(items, visited, temp);
				temp.pop_back();
				items.left[item[0]]++;items.right[item[0]]--;
				items.left[item[1]]++;items.right[item[1]]--;
			}
		}
	}
	else if(items.turn==-1)
	{
		items.turn = 1;
		for(int i=0;i<3;i++)
		{
			if(items.right[i]>0)
			{
				items.right[i]--;
				items.left[i]++;
				temp.push_back(items);
				dfs(items,visited, temp);
				temp.pop_back();
				items.right[i]++;
				items.left[i]--;
			}
		}
	}
}

int main(int argc, char** argv)
{
	struct data items;
	int one = atoi(argv[1]);
	int two = atoi(argv[2]);
	int three = atoi(argv[3]);
	vector<vector<vector<int>>>visited(one+1,vector<vector<int>>(two+1,vector<int>(three+1,0)));
	items.left[0] = one;
	items.left[1] = two;
	items.left[2] = three;
	vector<struct data> temp;
	temp.push_back(items);
	dfs(items, visited, temp);

	//cout<<result.size()<<endl;
	for(int i=0;i<result.size();i++)
	{
		cout<<result[i].left[0]<<" "<<result[i].left[1]<<" "<<result[i].left[2]<<endl;
	}
}
