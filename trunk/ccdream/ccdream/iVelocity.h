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

/*
 == Simple User Guide ==
 Use:
	+ (int) initWithTemplate:(NSString *)temp;
 to load a VTL(velocity template file).
 
 Then generate render result by:
	+ (RenderStatus) renderBlockWithData:(NSMutableDictionary *)dictionaryData 
							returnString:(NSMutableString *)strResult;
 the result is return by strResult. and the data is saved or modified in dictionaryData.
 
 If you has confused with the syntax of template file that parsed by iVelocity, can use 
 print method to print the syntax structure in Console. 
	+ (void) print;
 */

#import <Foundation/Foundation.h>

typedef enum {
	RENDER_OK = 0,
	RENDER_ERROR
} RenderStatus;

@interface iVelocity : NSObject {
@private
	id	_localCache;
	BOOL _isParsed;
}

+ (NSString *) version;

- (id) initWithTemplate:(NSString *)temp // VTL String
				 withKey:(NSString *)key // VTL String Name, used for cache.
			  forceFlush:(BOOL)mustflush;// YES: parse the VTL even if cache match!
										// NO: used the cached object if match.

- (id) initWithFile:(NSString *)filename// xxx.vm file that support VTL
		  forceFlush:(BOOL)mustflush;	// YES: parse the VTL even if cache match!
										// NO: used the cached object if match.

- (void) print;

- (RenderStatus) renderBlockWithData:(NSMutableDictionary *)dictionaryData 
						returnString:(NSMutableString *)strResult;

@end
