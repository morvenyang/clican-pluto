//
//  PhotoProcess.m
//  appletvserver
//
//  Created by zhang wei on 13-3-29.
//
//

#import "PhotoProcess.h"
#import "AppDelegate.h"
@implementation PhotoProcess
@synthesize assets = _assets;
@synthesize library = _library;

-(id) init{
    self =[super init];
    if(self){
        self.assets = [[[NSMutableArray alloc] init] autorelease];
        self.library = [[[ALAssetsLibrary alloc] init] autorelease];
        [self.library enumerateGroupsWithTypes:ALAssetsGroupSavedPhotos
                                    usingBlock:^(ALAssetsGroup *group, BOOL *stop){
                                        if(group != NULL) {
                                            [group enumerateAssetsUsingBlock:^(ALAsset *result, NSUInteger index, BOOL *stop){
                                                if(result != NULL) {
                                                    NSLog(@"See Asset: %@", result);
                                                    [self.assets addObject:result];
                                                }
                                            }];
                                        }
                                    }
                                  failureBlock: ^(NSError *error) {
                                      NSLog(@"Failure");
                                  }];
    }
    return self;
}
-(NSString*) loadPhotos{
    NSString* result = @"[";
    for (int i=0; i<[self.assets count]; i++) {
        ALAsset *asset = [self.assets objectAtIndex:i];
        NSString* url;
        if([[[asset defaultRepresentation] UTI] isEqualToString:@"public.png"]){
            url = [NSString stringWithFormat:@"http://%@:8080/appletv/noctl/photo/detail.png?id=%i",AppDele.ipAddress,i];
        }else if([[[asset defaultRepresentation] UTI] isEqualToString:@"public.jpeg"]){
            url = [NSString stringWithFormat:@"http://%@:8080/appletv/noctl/photo/detail.jpeg?id=%i",AppDele.ipAddress,i];
        }else{
            url = [NSString stringWithFormat:@"http://%@:8080/appletv/noctl/photo/detail.jpg?id=%i",AppDele.ipAddress,i];
        }
        if(i==[self.assets count]-1){
            result = [result stringByAppendingFormat:@"\"%@\"",url];
        }else{
            result = [result stringByAppendingFormat:@"\"%@\",",url];
        }
    }
    result = [result stringByAppendingString:@"]"];
    return result;
}

-(NSData*) readPhoto:(NSString*) idStr{
    int idInt = idStr.intValue;
    if(idInt<[self.assets count]){
        ALAsset *asset = [self.assets objectAtIndex:idInt];
        NSDictionary* metadata = [[asset defaultRepresentation] metadata];
        for(NSString* key in metadata.keyEnumerator){
            NSLog(@"%@=%@",key,[metadata objectForKey:key]);
        }
        ALAssetRepresentation *rep = [asset defaultRepresentation];
        Byte *buffer = (Byte*)malloc(rep.size);
        NSUInteger buffered = [rep getBytes:buffer fromOffset:0.0 length:rep.size error:nil];
        NSData *data = [NSData dataWithBytesNoCopy:buffer length:buffered freeWhenDone:YES];
        return data;
    }else{
        return nil;
    }
}
- (void)dealloc
{
    
    TT_RELEASE_SAFELY(_assets);
    TT_RELEASE_SAFELY(_library);
 
    [super dealloc];
}
@end
