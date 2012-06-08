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

#import <Foundation/Foundation.h>
#import "CoreInterface.h"
#import "NameLevel.h"

@interface NameBlock : NSObject<Render, Expression, Name,Block> {

	ValueType valueType; //! set when runtime.
	id value;			 //! set when runtime.
	NSString *name;		 //! set when runtime.
	
	NameLevel *nameLevel;
}

+ (ValueType)checkValueType:(id)value;
- (void) initNameWithData:(NSMutableDictionary *)dictionaryData;
- (void) reset;

- (NSString *) getNamePrefix;

@end
