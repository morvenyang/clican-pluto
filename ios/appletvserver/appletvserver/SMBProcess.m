//
//  SMBProcess.m
//  appletvserver
//
//  Created by zhang wei on 13-4-10.
//
//

#import "SMBProcess.h"

@implementation SMBProcess
@synthesize smbFile = _smbFile;
@synthesize smbPath = _smbPath;

-(void) dealloc{
    TT_RELEASE_SAFELY(_smbPath);
    TT_RELEASE_SAFELY(_smbFile);
    [super dealloc];
}
@end
