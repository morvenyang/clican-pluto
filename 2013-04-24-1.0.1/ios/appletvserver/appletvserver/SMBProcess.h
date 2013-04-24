//
//  SMBProcess.h
//  appletvserver
//
//  Created by zhang wei on 13-4-10.
//
//

#import <Foundation/Foundation.h>
#import "KxSMBProvider.h"
@interface SMBProcess : NSObject{
    KxSMBItemFile* _smbFile;
    NSString* _smbPath;
}
@property (nonatomic, retain) KxSMBItemFile *smbFile;
@property (nonatomic, retain) NSString *smbPath;
-(NSString*)getResourcesForParent:(NSString*) parent;
@end
