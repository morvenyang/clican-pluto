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

#import "iVelocity.h"
#import "TransLayer.h"
#import "StatementBlock.h"
#import "LocalCache.h"

@implementation iVelocity

- (id) initWithTemplate:(NSString *)temp
				withKey:(NSString *)key 
			 forceFlush:(BOOL)mustflush
{
	self = [super init];
	if (self) {
		_isParsed = NO;
		_localCache = [LocalCache createAndGetLocalCache:key forceFlush:mustflush];
		LocalCache *lc = (LocalCache *)_localCache;
		if (!lc.statementCache) {
			initLex(temp, _localCache);
			int res = yyparse();
			if (res) {
				NSLog(@"Parse error!");
			} else {
				_isParsed = YES;
			}
		} else {
			_isParsed = YES;
		}

	}
	return self;
}

- (id) initWithFile:(NSString *)filename
		 forceFlush:(BOOL)mustflush
{
	self = [super init];
	if (self) {
		_isParsed = NO;
		_localCache = [LocalCache createAndGetLocalCache:filename forceFlush:mustflush];
		LocalCache *lc = (LocalCache *)_localCache;
		if (!lc.statementCache) {
			initWithTemplateFile(filename, _localCache);
			int res = yyparse();
			if (res) {
				NSLog(@"Parse error!");
			} else {
				_isParsed = YES;
			}
		} else {
			_isParsed = YES;
		}

	}
	return self;
}

- (void) print 
{
	if(_isParsed) {
		printStatements();
	}
}

- (RenderStatus) renderBlockWithData:(NSMutableDictionary *)dictionaryData 
						returnString:(NSMutableString *)strResult
{
	if (_isParsed) {
		LocalCache *lc = (LocalCache *)_localCache;
		
		[lc initWithDictionary:dictionaryData];
		
		StatementBlock *block = lc.statementCache;
		return [block renderBlockWithData:dictionaryData 
							 returnString:strResult];
	} else {
		return RENDER_ERROR;
	}

}

+ (NSString *) version
{
	return @"0.3";
}

- (void)dealloc
{
	[super dealloc];
}

@end
