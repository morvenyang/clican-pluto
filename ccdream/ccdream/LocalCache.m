/*
 Copyright [2011] [chunyi zhou]
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

#import "LocalCache.h"
#import "NameBlock.h"

@implementation LocalCache
@synthesize nameCache;
@synthesize statementCache;

static NSMutableDictionary *GlobalLocalCache;

- (void) resetAllName
{
	NSArray *nameKeyArray = [nameCache allKeys];
	for (int i=0; i<nameKeyArray.count; i++) {
		NSString *namekey = [nameKeyArray objectAtIndex:i];
		
		NameBlock *name = [nameCache valueForKey:namekey];
		
		[name reset];
	}
}

- (void) initWithDictionary:(NSMutableDictionary *)dic
{
	NSArray *nameKeyArray = [nameCache allKeys];
	for (int i=0; i<nameKeyArray.count; i++) {
		NSString *namekey = [nameKeyArray objectAtIndex:i];
		
		NameBlock *name = [nameCache valueForKey:namekey];
		
		[name initNameWithData:dic];
	}
}

+ (id) createAndGetLocalCache:(NSString *)key 
				   forceFlush:(BOOL)mustflush
{
	if (!GlobalLocalCache) {
		GlobalLocalCache = [[NSMutableDictionary alloc] init];
	}
	
	LocalCache *lc = [GlobalLocalCache valueForKey:key];
	if (lc) {
		
		if (mustflush) {
			[GlobalLocalCache removeObjectForKey:key];
			
		} else {
			[lc resetAllName];
			return lc;
		}
	}
	
	lc = [[LocalCache alloc] init];
	
	[GlobalLocalCache setObject:lc forKey:key];
	
	[lc release];
	
	[lc resetAllName];
	
	return lc;
}

- (void)dealloc
{
	[nameCache release];
	[statementCache release];
	[super dealloc];
}

@end
